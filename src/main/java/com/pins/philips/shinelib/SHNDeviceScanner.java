package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.pins.philips.shinelib.framework.BleDeviceFoundInfo;
import com.pins.philips.shinelib.framework.LeScanCallbackProxy;
import com.pins.philips.shinelib.framework.SingleThreadEventDispatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner implements LeScanCallbackProxy.LeScanCallback, SingleThreadEventDispatcher.EventHandler {
    private static final String TAG = SHNDeviceScanner.class.getSimpleName();
    private static final boolean LOGGING = false;

    private final SingleThreadEventDispatcher eventDispatcher;
    private Set<String> macAddressesOfFoundDevices = new HashSet<>();
    private List<LeScanCallbackProxy> leScanCallbackProxies = new ArrayList<>();
    private SHNDeviceScannerListener shnDeviceScannerListener;

    static class DeviceFoundEvent extends SingleThreadEventDispatcher.BaseEvent {
        public final BleDeviceFoundInfo bleDeviceFoundInfo;

        public DeviceFoundEvent(BleDeviceFoundInfo bleDeviceFoundInfo) {
            this.bleDeviceFoundInfo = bleDeviceFoundInfo;
        }
    }

    public interface SHNDeviceScannerListener {
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDevice shnDevice);
        public void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    private final SHNCentral shnCentral;

    public SHNDeviceScanner(SHNCentral shnCentral) {
        this.shnCentral = shnCentral;
        this.eventDispatcher = this.shnCentral.getEventDispatcher();
        this.eventDispatcher.register(this, DeviceFoundEvent.class);
    }

    public boolean startScanning(SHNDeviceScannerListener shnDeviceScannerListener) {
        macAddressesOfFoundDevices.clear();
        leScanCallbackProxies.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
        for (SHNDeviceDefinitionInfo deviceDefinitionInfo: shnCentral.getRegisteredDeviceDefinitions()) {
            Set<UUID> primaryServiceUUIDs = deviceDefinitionInfo.getPrimaryServiceUUIDs();
            LeScanCallbackProxy leScanCallbackProxy = new LeScanCallbackProxy();
            leScanCallbackProxy.startLeScan(primaryServiceUUIDs.toArray(new UUID[primaryServiceUUIDs.size()]), this, deviceDefinitionInfo);
            leScanCallbackProxies.add(leScanCallbackProxy);
        }
        // TODO monitor the start results and if needed stop scanning....
        return true;
    }

    public void stopScanning(SHNDeviceScannerListener shnDeviceScannerListener) {
        for (LeScanCallbackProxy leScanCallbackProxy: leScanCallbackProxies) {
            leScanCallbackProxy.stopLeScan(this);
        }
        leScanCallbackProxies.clear();
    }

    void onLeScanSub(BleDeviceFoundInfo bleDeviceFoundInfo) {
        eventDispatcher.queueEvent(new DeviceFoundEvent(bleDeviceFoundInfo));
    }

    boolean handleDeviceFoundEvent(DeviceFoundEvent deviceFoundEvent) {
        BleDeviceFoundInfo bleDeviceFoundInfo = deviceFoundEvent.bleDeviceFoundInfo;
        if (!macAddressesOfFoundDevices.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            macAddressesOfFoundDevices.add(bleDeviceFoundInfo.getDeviceAddress());
            final SHNDevice shnDevice = new SHNDevice(bleDeviceFoundInfo, shnCentral);

            shnCentral.reportDeviceFound(shnDeviceScannerListener, SHNDeviceScanner.this, shnDevice);
        }

        return true; // Event handled
    }

    public void shutdown() {
        eventDispatcher.unregister(this);
    }

    // SHNDeviceScanner.LeScanCallback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord, Object callbackParameter) {
        if (LOGGING) Log.e(TAG, "onLeScan");
        onLeScanSub(new BleDeviceFoundInfo(device, rssi, scanRecord));
    }

    // SingleThreadEventDispatcher.EventHandler
    @Override
    public boolean handleEvent(SingleThreadEventDispatcher.BaseEvent event) {
        if (LOGGING) Log.e(TAG, "handleEvent");
        if (event instanceof DeviceFoundEvent) {
            DeviceFoundEvent deviceFoundEvent = (DeviceFoundEvent)event;
            return handleDeviceFoundEvent((DeviceFoundEvent)event);
        }
        return false;
    }

//    private String byteArrayToString(byte[] scanRecord) {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append('[');
//
//        for (byte b: scanRecord) {
//            if (sb.length() > 1) sb.append(',');
//            sb.append(String.format("%1$02X", b));
//        }
//        sb.append(']');
//
//        return sb.toString();
//    }
//
//    private void parseScanRecord(byte[] scanRecord) {
//        Log.e(TAG, "parseScanRecord start");
//        int start = 0;
//        int length = scanRecord[start];
//        while(length > 0) {
//            Log.e(TAG, "len: 0x" + String.format("%1$02X", length) + " AD Type: 0x" + String.format("%1$02X", scanRecord[start + 1]) + " AD Data: " + byteArrayToString(Arrays.copyOfRange(scanRecord, start + 2, start + length + 1) ));
//            start += length + 1; // length excluding length indicator
//            length = scanRecord[start];
//        }
//        Log.e(TAG, "parseScanRecord stop");
//    }
}
