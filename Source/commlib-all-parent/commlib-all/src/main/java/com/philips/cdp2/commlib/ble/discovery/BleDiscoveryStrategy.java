/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.BleDeviceCache.CacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation.SHNDeviceInformationType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed;
import static com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber;

public class BleDiscoveryStrategy extends ObservableDiscoveryStrategy implements SHNDeviceScanner.SHNDeviceScannerListener {

    private static final String TAG = "BleDiscoveryStrategy";

    /**
     * Chosen at 60 seconds to have a sufficiently long window during which a discovery/advertisement
     * collision is expected to occur.
     * <p>
     * See also: <a href="https://www.beaconzone.co.uk/ibeaconadvertisinginterval">iBeacon Advertising Interval</a>
     */
    public static final long SCAN_WINDOW_MILLIS = 60000L;

    public static final byte[] MANUFACTURER_PREAMBLE = {(byte) 0xDD, 0x01};

    private final Context context;
    private final BleDeviceCache bleDeviceCache;
    private final SHNDeviceScanner deviceScanner;
    private Set<String> modelIds;
    private ScheduledExecutorService executor;
    private ScheduledFuture discoveryFuture;
    private Set<String> discoveredMacAddresses = new CopyOnWriteArraySet<>();

    private ExpirationCallback expirationCallback = new ExpirationCallback() {
        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            final CacheData cacheData = bleDeviceCache.getCacheData(networkNode.getCppId());
            if (cacheData == null) {
                return;
            }

            if (cacheData.getDevice().getState() == Disconnected) {
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
        this.discoveredMacAddresses.clear();
        this.modelIds = modelIds;

        if (checkAndroidPermission(this.context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery via BLE is missing permission: " + ACCESS_COARSE_LOCATION);
        }

        discoveryFuture = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!deviceScanner.startScanning(BleDiscoveryStrategy.this, DuplicatesAllowed, SCAN_WINDOW_MILLIS)) {
                    throw new TransportUnavailableException("Error starting scanning via BLE.");
                }
            }
        }, 0L, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        deviceScanner.stopScanning();
        discoveredMacAddresses.clear();

        if (discoveryFuture != null) {
            discoveryFuture.cancel(true);
        }
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        // Ignored
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();
        final SHNCapabilityDeviceInformation deviceInformation = device.getCapability(SHNCapabilityDeviceInformation.class);
        if (deviceInformation == null) {
            throw new IllegalArgumentException("BLE device doesn't expose device information.");
        }

        String address = device.getAddress();

        if (discoveredMacAddresses.contains(address)) {
            return;
        }

        final String modelId = createModelId(shnDeviceFoundInfo);

        if (modelIds.isEmpty() || modelIds.contains(modelId)) {
            discoveredMacAddresses.add(device.getAddress());

            final NetworkNode networkNode = new NetworkNode();
            networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
            networkNode.setModelId(modelId);
            networkNode.setModelName(device.getDeviceTypeName());

            networkNode.setBleAddress(device.getAddress());
            networkNode.setName(shnDeviceFoundInfo.getDeviceName());

            device.registerSHNDeviceListener(new DeviceListener(device, deviceInformation, networkNode));
            device.connect();
        }
    }

    @VisibleForTesting
    int checkAndroidPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    private class DeviceListener implements SHNDevice.SHNDeviceListener {
        final AtomicInteger counter = new AtomicInteger(1);
        @NonNull
        private final SHNDevice device;
        private final SHNCapabilityDeviceInformation deviceInformation;
        private final NetworkNode networkNode;

        private final SHNCapabilityDeviceInformation.Listener deviceInformationListener = new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull SHNDeviceInformationType deviceInformationType, @NonNull String value, @NonNull Date dateWhenAcquired) {
                networkNode.setCppId(value);
                bleDeviceCache.addDevice(device, networkNode, expirationCallback, SCAN_WINDOW_MILLIS);
                discoveredMacAddresses.remove(networkNode.getBleAddress());
                notifyNetworkNodeDiscovered(networkNode);

                onDeviceResponse();
            }

            @Override
            public void onError(@NonNull SHNDeviceInformationType deviceInformationType, @NonNull SHNResult error) {
                DICommLog.e(TAG, "Device [" + device.getAddress() + "] error: " + error.toString());

                onDeviceResponse();
            }
        };

        DeviceListener(final @NonNull SHNDevice device, SHNCapabilityDeviceInformation deviceInformation, NetworkNode networkNode) {
            this.device = device;
            this.deviceInformation = deviceInformation;
            this.networkNode = networkNode;
        }

        @Override
        public void onStateUpdated(final SHNDevice shnDevice) {
            if (shnDevice.getState() == Connected) {
                deviceInformation.readDeviceInformation(SerialNumber, deviceInformationListener);
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            DICommLog.e(TAG, "Device [" + shnDevice.getAddress() + "] failed to connect: " + result.toString());

            device.unregisterSHNDeviceListener(this);
            discoveredMacAddresses.remove(shnDevice.getAddress());
        }

        @Override
        public void onReadRSSI(int rssi) {
            // Ignored
        }

        private void onDeviceResponse() {
            if (counter.decrementAndGet() == 0) {
                device.unregisterSHNDeviceListener(this);
                device.disconnect();
            }
        }
    }

    @Nullable
    private String createModelId(@NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        byte[] manufacturerData = shnDeviceFoundInfo.getBleScanRecord().getManufacturerSpecificData();
        if (manufacturerData != null && Arrays.equals(Arrays.copyOfRange(manufacturerData, 0, 2), MANUFACTURER_PREAMBLE)) {
            return new String(Arrays.copyOfRange(manufacturerData, 2, manufacturerData.length));
        }
        return null;
    }
}
