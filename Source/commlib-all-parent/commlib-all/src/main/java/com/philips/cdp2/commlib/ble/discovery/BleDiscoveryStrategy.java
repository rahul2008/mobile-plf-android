/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.discovery.ObservableDiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.Set;

public class BleDiscoveryStrategy extends ObservableDiscoveryStrategy implements SHNDeviceScanner.SHNDeviceScannerListener {

    private final Context context;
    private final BleDeviceCache bleDeviceCache;
    private final long timeoutMillis;
    private final SHNDeviceScanner deviceScanner;

    public BleDiscoveryStrategy(@NonNull Context context, @NonNull BleDeviceCache bleDeviceCache, @NonNull SHNDeviceScanner deviceScanner, long timeoutMillis) {
        this.context = context;
        this.bleDeviceCache = bleDeviceCache;
        this.timeoutMillis = timeoutMillis;
        this.deviceScanner = deviceScanner;
    }

    @Override
    public void start() throws MissingPermissionException, TransportUnavailableException {
        start(null);
    }

    @Override
    public void start(Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException {
        if (checkAndroidPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new MissingPermissionException("Discovery via BLE is missing permission: " + Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (deviceScanner.startScanning(this, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, timeoutMillis)) {
            notifyDiscoveryStarted();
        } else {
            throw new TransportUnavailableException("Error starting scanning via BLE.");
        }
    }

    @VisibleForTesting
    int checkAndroidPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    @Override
    public void stop() {
        deviceScanner.stopScanning();
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();

        final NetworkNode networkNode = createNetworkNode(device);
        if (networkNode == null) {
            return;
        }

        bleDeviceCache.addDevice(device);
        notifyNetworkNodeDiscovered(networkNode);
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
        networkNode.setModelId(null); // TODO model id, e.g. 'FC8932'
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        return networkNode;
    }
}
