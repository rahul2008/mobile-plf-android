package com.philips.cdp2.commlib.discovery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;

import java.util.Collection;

public final class BleDiscoveryStrategy implements DiscoveryStrategy, SHNDeviceScanner.SHNDeviceScannerListener {

    private DiscoveryListener discoveryListener;
    private final BleDeviceCache bleDeviceCache;
    private final long timeoutMillis;
    private final SHNDeviceScanner deviceScanner;

    private SHNDevice.SHNDeviceListener deviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            if (SHNDevice.State.Connected.equals(shnDevice.getState())) {

                if (discoveryListener != null) {
                    final NetworkNode networkNode = createNetworkNode(shnDevice);

                    if (networkNode != null) {
                        bleDeviceCache.addDevice(shnDevice);
                        discoveryListener.onNetworkNodeDiscovered(networkNode);
                    }
                }
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {
            // NOOP
        }

        @Override
        public void onReadRSSI(int i) {
            // NOOP
        }
    };

    public BleDiscoveryStrategy(@NonNull BleDeviceCache bleDeviceCache, @NonNull SHNDeviceScanner deviceScanner, long timeoutMillis) {
        this.bleDeviceCache = bleDeviceCache;
        this.timeoutMillis = timeoutMillis;
        this.deviceScanner = deviceScanner;
    }

    @Override
    public void start(Context context, DiscoveryListener discoveryListener) throws MissingPermissionException {
        start(context, discoveryListener, null);
    }

    @Override
    public void start(Context context, @NonNull DiscoveryListener discoveryListener, Collection<String> deviceTypes) throws MissingPermissionException {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery over BLE is missing permission: " + Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        this.discoveryListener = discoveryListener;

        deviceScanner.startScanning(this, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, timeoutMillis);
        this.discoveryListener.onDiscoveryStarted();
    }

    @Override
    public void stop() {
        deviceScanner.stopScanning();
        this.discoveryListener.onDiscoveryFinished();
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();

        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        this.discoveryListener.onDiscoveryFinished();
    }

    private NetworkNode createNetworkNode(final SHNDevice shnDevice) {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setBootId(-1L);
        networkNode.setCppId(shnDevice.getAddress()); // TODO cloud identifier; hijacked MAC address for now
        networkNode.setName(shnDevice.getName()); // TODO Friendly name, e.g. 'Vacuum cleaner'
        networkNode.setModelName(shnDevice.getDeviceTypeName()); // TODO model name, e.g. 'Polaris'
        networkNode.setModelType(null); // TODO model type, e.g. 'FC8932'
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        return networkNode;
    }
}
