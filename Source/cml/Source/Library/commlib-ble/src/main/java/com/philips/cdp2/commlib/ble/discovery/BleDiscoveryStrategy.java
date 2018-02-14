/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed;

public class BleDiscoveryStrategy extends ObservableDiscoveryStrategy implements SHNDeviceScanner.SHNDeviceScannerListener {

    /**
     * Chosen at 60 seconds to have a sufficiently long window during which a discovery/advertisement
     * collision is expected to occur.
     * <p>
     * See also: <a href="https://www.beaconzone.co.uk/ibeaconadvertisinginterval">iBeacon Advertising Interval</a>
     */
    public static final long SCAN_WINDOW_MILLIS = 60000L;

    /**
     * This is a worldwide constant, see: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
     */
    public static final int MANUFACTURER_PREAMBLE = 477; // 0x01DD

    private final Context context;
    private final BleDeviceCache bleDeviceCache;
    private final SHNDeviceScanner deviceScanner;
    private ScheduledExecutorService executor;
    private ScheduledFuture discoveryStoppedFuture;

    @VisibleForTesting
    Set<String> modelIds;

    private ExpirationCallback expirationCallback = new ExpirationCallback() {
        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            final BleCacheData cacheData = bleDeviceCache.getCacheData(networkNode.getCppId());
            if (cacheData == null) {
                return;
            }

            if (cacheData.getDevice().getState() == Disconnected) {
                cacheData.setAvailable(false);
                notifyNetworkNodeLost(networkNode);
            } else {
                cacheData.resetTimer();
            }
        }
    };

    public BleDiscoveryStrategy(@NonNull Context context, @NonNull BleDeviceCache bleDeviceCache, @NonNull SHNDeviceScanner deviceScanner) {
        this.context = context;
        this.bleDeviceCache = bleDeviceCache;
        this.deviceScanner = deviceScanner;
        this.modelIds = new HashSet<>();
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start() throws MissingPermissionException, TransportUnavailableException {
        start(Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException {
        start(deviceTypes, Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes, @NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException {
        this.modelIds = modelIds;

        if (checkAndroidPermission(this.context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery via BLE is missing permission: " + ACCESS_COARSE_LOCATION);
        }

        discoveryStoppedFuture = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!deviceScanner.startScanning(BleDiscoveryStrategy.this, DuplicatesAllowed, SCAN_WINDOW_MILLIS)) {
                    throw new TransportUnavailableException("Error starting scanning via BLE.");
                }
                notifyDiscoveryStarted();
            }
        }, 0L, 30, TimeUnit.SECONDS);
    }

    @VisibleForTesting
    public int checkAndroidPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    @Override
    public void stop() {
        deviceScanner.stopScanning();

        if (discoveryStoppedFuture != null) {
            discoveryStoppedFuture.cancel(true);
        }
    }

    @Override
    public synchronized void clearDiscoveredNetworkNodes() {
        Collection<BleCacheData> items = bleDeviceCache.clear();

        for (BleCacheData item : items) {
            notifyNetworkNodeLost(item.getNetworkNode());
        }
    }

    private NetworkNode createNetworkNode(final SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final NetworkNode networkNode = new NetworkNode();

        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();
        networkNode.setBootId(-1L);
        networkNode.setCppId(device.getAddress()); // Cloud identifier; hijacked MAC address for now
        networkNode.setName(device.getName()); // Friendly name, e.g. 'Vacuum cleaner'
        networkNode.setDeviceType(device.getDeviceTypeName()); // Model name, e.g. 'Polaris'

        // Model id, e.g. 'FC8932'
        byte[] manufacturerData = shnDeviceFoundInfo.getBleScanRecord().getManufacturerSpecificData(MANUFACTURER_PREAMBLE);
        if (manufacturerData != null) {
            final String modelId = new String(manufacturerData);
            networkNode.setModelId(modelId);
        }
        return networkNode;
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final NetworkNode networkNode = createNetworkNode(shnDeviceFoundInfo);
        if (networkNode == null) {
            return;
        }

        if (!modelIds.isEmpty() && !modelIds.contains(networkNode.getModelId())) {
            return;
        }

        if (bleDeviceCache.contains(networkNode.getCppId())) {
            BleCacheData cacheData = bleDeviceCache.getCacheData(networkNode.getCppId());
            cacheData.resetTimer();
            cacheData.setAvailable(true);
        } else {
            bleDeviceCache.addDevice(shnDeviceFoundInfo.getShnDevice(), networkNode, expirationCallback, SCAN_WINDOW_MILLIS);
        }
        notifyNetworkNodeDiscovered(networkNode);
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        notifyDiscoveryStopped();
    }
}
