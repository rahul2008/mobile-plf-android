package com.philips.cdp2.commlib.discovery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.discovery.strategy.ObservableDiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.SHNResult;

import java.util.Set;

public final class BleDiscoveryStrategy extends ObservableDiscoveryStrategy implements SHNDeviceScanner.SHNDeviceScannerListener {

    private final Context context;
    private final BleDeviceCache bleDeviceCache;
    private final long timeoutMillis;
    private final SHNDeviceScanner deviceScanner;

    private SHNDevice.SHNDeviceListener deviceListener = new SHNDevice.SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {

            final NetworkNode networkNode = createNetworkNode(shnDevice);
            if (networkNode == null) {
                return;
            }

            if (SHNDevice.State.Connected.equals(shnDevice.getState())) {
                bleDeviceCache.addDevice(shnDevice);
                notifyNetworkNodeDiscovered(networkNode);
            } else if (SHNDevice.State.Disconnected.equals(shnDevice.getState())) {
                notifyNetworkNodeLost(networkNode);
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

    public BleDiscoveryStrategy(@NonNull Context context, @NonNull BleDeviceCache bleDeviceCache, @NonNull SHNDeviceScanner deviceScanner, long timeoutMillis) {
        this.context = context;
        this.bleDeviceCache = bleDeviceCache;
        this.timeoutMillis = timeoutMillis;
        this.deviceScanner = deviceScanner;
    }

    @Override
    public void start() throws MissingPermissionException {
        start(null);
    }

    @Override
    public void start(Set<String> deviceTypes) throws MissingPermissionException {
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery over BLE is missing permission: " + Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        deviceScanner.startScanning(this, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, timeoutMillis);
        notifyDiscoveryStarted();
    }

    @Override
    public void stop() {
        deviceScanner.stopScanning();
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();

        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        notifyDiscoveryStopped();
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
