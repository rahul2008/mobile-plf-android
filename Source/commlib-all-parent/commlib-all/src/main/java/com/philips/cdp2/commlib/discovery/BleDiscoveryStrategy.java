package com.philips.cdp2.commlib.discovery;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

public class BleDiscoveryStrategy implements DiscoveryStrategy, SHNDeviceScanner.SHNDeviceScannerListener {

    private final SHNDeviceScanner deviceScanner;
    private final long timeoutMillis;
    private DiscoveryListener discoveryListener;

    public BleDiscoveryStrategy(SHNDeviceScanner deviceScanner, @NonNull long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        this.deviceScanner = deviceScanner;
    }

    @Override
    public void start(@NonNull DiscoveryListener discoveryListener) {
        // TODO check BLE permissions, notify

        this.discoveryListener = discoveryListener;
        this.discoveryListener.onDiscoveryStarted();

        this.deviceScanner.startScanning(this, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, timeoutMillis);
    }

    @Override
    public void stop() {
        this.deviceScanner.stopScanning();
        this.discoveryListener.onDiscoveryFinished();
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final NetworkNode networkNode = createNetworkNode(shnDeviceFoundInfo.getShnDevice());
        this.discoveryListener.onNetworkNodeDiscovered(networkNode);
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        this.discoveryListener.onDiscoveryFinished();
    }

    private NetworkNode createNetworkNode(final SHNDevice shnDevice) {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setBootId(-1L);
        networkNode.setCppId(shnDevice.getAddress()); // TODO cloud identifier; hijacked address for now
        networkNode.setName(shnDevice.getName()); // TODO Friendly name
        networkNode.setModelName(shnDevice.getDeviceTypeName()); // TODO model name, e.g. Polaris
        networkNode.setModelType(null); // TODO model type, e.g. FC8932
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        return networkNode;
    }
}
