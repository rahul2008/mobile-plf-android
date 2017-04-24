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
import com.philips.cdp2.bluelib.plugindefinition.StreamingCapability;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.BleDeviceCache.CacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.VerboseRunnable;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation.SHNDeviceInformationType;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolByteStreamingVersionSwitcher;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import com.philips.pins.shinelib.services.SHNServiceDiCommStreaming;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private final SHNCentral shnCentral;
    private Set<String> modelIds;

    private ScheduledExecutorService scanExecutor;
    private ScheduledThreadPoolExecutor discoveryExecutor;

    private ScheduledFuture scanFuture;
    private Set<String> discoveredMacAddresses = new CopyOnWriteArraySet<>();

    private final ExpirationCallback expirationCallback = new ExpirationCallback() {
        @Override
        public void onCacheExpired(NetworkNode networkNode) {
            final CacheData cacheData = bleDeviceCache.findByCppId(networkNode.getCppId());
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

    public BleDiscoveryStrategy(@NonNull Context context, @NonNull BleDeviceCache bleDeviceCache, @NonNull SHNDeviceScanner deviceScanner, @NonNull SHNCentral shnCentral) {
        this.context = context;
        this.bleDeviceCache = bleDeviceCache;
        this.deviceScanner = deviceScanner;
        this.shnCentral = shnCentral;
        this.modelIds = new HashSet<>();
        this.scanExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final CacheData cacheData = bleDeviceCache.findByAddress(shnDeviceFoundInfo.getDeviceAddress());
        if (cacheData == null) {
            onDeviceFound(shnDeviceFoundInfo);
        } else {
            bleDeviceCache.add(cacheData.getDevice(), cacheData.getNetworkNode(), expirationCallback, SCAN_WINDOW_MILLIS);
        }
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        // Ignored
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

        startDiscovery();
        startBleScan();
    }

    @Override
    public void stop() {
        stopBleScan();
        stopDiscovery();
    }

    @VisibleForTesting
    int checkAndroidPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    private void startBleScan() {
        scanFuture = scanExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!deviceScanner.startScanning(BleDiscoveryStrategy.this, DuplicatesAllowed, SCAN_WINDOW_MILLIS)) {
                    throw new TransportUnavailableException("Error starting scanning via BLE.");
                }
            }
        }, 0L, 30, TimeUnit.SECONDS);
    }

    private void stopBleScan() {
        deviceScanner.stopScanning();
        if (scanFuture != null) {
            scanFuture.cancel(true);
        }
    }

    private void startDiscovery() throws MissingPermissionException {
        if (checkAndroidPermission(this.context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery via BLE is missing permission: " + ACCESS_COARSE_LOCATION);
        }
        this.discoveredMacAddresses.clear();
        discoveryExecutor = new ScheduledThreadPoolExecutor(1);
    }

    private void stopDiscovery() {
        if (discoveryExecutor != null) {
            List<Runnable> tasks = discoveryExecutor.shutdownNow();
            DICommLog.w(TAG, "Rejected discovery tasks: " + tasks.size());
        }
        discoveredMacAddresses.clear();
    }

    private void onDeviceFound(@NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();

        // Check for DIS
        final SHNCapabilityDeviceInformation deviceInformation = device.getCapability(SHNCapabilityDeviceInformation.class);
        if (deviceInformation == null) {
            throw new IllegalArgumentException("BLE device doesn't expose device information.");
        }

        // Store MAC address to prevent duplicates
        final String address = device.getAddress();
        if (discoveredMacAddresses.contains(address)) {
            return;
        }

        // Apply model id filter
        final String modelId = createModelId(shnDeviceFoundInfo);
        if (modelIds.isEmpty() || modelIds.contains(modelId)) {
            DICommLog.d(TAG, String.format(Locale.US, "Device found: [%s], signal strength: [%d dBm]", shnDeviceFoundInfo.getDeviceAddress(), shnDeviceFoundInfo.getRssi()));
            discoveredMacAddresses.add(device.getAddress());

            discoveryExecutor.execute(new VerboseRunnable(new DiscoveryTask(device, deviceInformation, modelId)));
        } else {
            DICommLog.w(TAG, "Unhandled model id: " + modelId);
        }
    }

    private SHNDevice addDiCommStreamingToDevice(final @NonNull SHNDevice device) {
        SHNDeviceWrapper wrapper = (SHNDeviceWrapper) device;
        SHNDeviceImpl shnDevice = (SHNDeviceImpl) wrapper.getInternalDevice();

        if (shnDevice.getCapability(CapabilityDiComm.class) == null) {
            final SHNServiceDiCommStreaming shnServiceDiCommStreaming = new SHNServiceDiCommStreaming();
            shnDevice.registerService(shnServiceDiCommStreaming);

            final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming = new SHNProtocolByteStreamingVersionSwitcher(shnServiceDiCommStreaming, shnCentral.getInternalHandler());
            shnServiceDiCommStreaming.setShnServiceMoonshineStreamingListener(shnProtocolMoonshineStreaming);

            CapabilityDiComm capabilityDiComm = new StreamingCapability(shnProtocolMoonshineStreaming);
            shnDevice.registerCapability(CapabilityDiComm.class, capabilityDiComm);
        }
        return device;
    }

    @Nullable
    private String createModelId(@NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        byte[] manufacturerData = shnDeviceFoundInfo.getBleScanRecord().getManufacturerSpecificData();
        if (manufacturerData != null && Arrays.equals(Arrays.copyOfRange(manufacturerData, 0, 2), MANUFACTURER_PREAMBLE)) {
            return new String(Arrays.copyOfRange(manufacturerData, 2, manufacturerData.length));
        }
        return null;
    }

    @VisibleForTesting
    class DiscoveryTask implements Runnable, SHNDevice.SHNDeviceListener {
        private final CountDownLatch latch = new CountDownLatch(1);

        private final NetworkNode networkNode;

        @NonNull
        private final SHNDevice device;
        private final SHNCapabilityDeviceInformation deviceInformation;

        private final SHNCapabilityDeviceInformation.Listener deviceInformationListener = new SHNCapabilityDeviceInformation.Listener() {
            @Override
            public void onDeviceInformation(@NonNull SHNDeviceInformationType deviceInformationType, @NonNull String value, @NonNull Date dateWhenAcquired) {
                networkNode.setCppId(value);
                bleDeviceCache.add(addDiCommStreamingToDevice(device), networkNode, expirationCallback, SCAN_WINDOW_MILLIS);
                notifyNetworkNodeDiscovered(networkNode);

                finish();
            }

            @Override
            public void onError(@NonNull SHNDeviceInformationType deviceInformationType, @NonNull SHNResult error) {
                DICommLog.e(TAG, String.format(Locale.US, "Device [%s] error: %s", device.getAddress(), error.toString()));

                finish();
            }
        };

        DiscoveryTask(final @NonNull SHNDevice device, final @NonNull SHNCapabilityDeviceInformation deviceInformation, final @Nullable String modelId) {
            this.device = device;
            this.deviceInformation = deviceInformation;

            final NetworkNode networkNode = new NetworkNode();
            networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
            networkNode.setModelId(modelId);
            networkNode.setModelName(device.getDeviceTypeName());
            networkNode.setBleAddress(device.getAddress());
            networkNode.setName(device.getName());

            this.networkNode = networkNode;
        }

        @Override
        public void onStateUpdated(final SHNDevice deviceWithUpdatedState) {
            if (deviceWithUpdatedState != device) {
                throw new IllegalArgumentException("The SHNDevice as argument is not the same as the SHNDevice in the field. This is probably a bug!");
            }

            if (device.getState() == Connected) {
                deviceInformation.readDeviceInformation(SerialNumber, deviceInformationListener);
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice device, SHNResult result) {
            DICommLog.e(TAG, String.format(Locale.US, "Device [%s] failed to connect: %s", device.getAddress(), result.toString()));

            finish();
        }

        @Override
        public void onReadRSSI(int ignored) {
        }

        @Override
        public void run() {
            device.registerSHNDeviceListener(this);
            device.connect();

            try {
                latch.await();
            } catch (InterruptedException e) {
                finish();
            }
        }

        private void finish() {
            device.unregisterSHNDeviceListener(this);
            device.disconnect();

            discoveredMacAddresses.remove(device.getAddress());
            latch.countDown();
        }
    }
}
