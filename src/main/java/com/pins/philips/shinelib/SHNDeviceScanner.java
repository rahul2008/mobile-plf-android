package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.pins.philips.shinelib.framework.BleDeviceFoundInfo;
import com.pins.philips.shinelib.framework.LeScanCallbackProxy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner implements LeScanCallbackProxy.LeScanCallback {
    private static final String TAG = SHNDeviceScanner.class.getSimpleName();
    private static final boolean LOGGING = false;

    private Set<String> macAddressesOfFoundDevices = new HashSet<>();
    private List<LeScanCallbackProxy> leScanCallbackProxies = new ArrayList<>();
    private SHNDeviceScannerListener shnDeviceScannerListener;

    public interface SHNDeviceScannerListener {
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo);
        public void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    private final SHNCentral shnCentral;

    public SHNDeviceScanner(SHNCentral shnCentral) {
        this.shnCentral = shnCentral;
    }

    public boolean startScanning(SHNDeviceScannerListener shnDeviceScannerListener) {
        macAddressesOfFoundDevices.clear();
        leScanCallbackProxies.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
//        for (SHNDeviceDefinitionInfo deviceDefinitionInfo: shnCentral.getRegisteredDeviceDefinitions()) {
//            Set<UUID> primaryServiceUUIDs = deviceDefinitionInfo.getPrimaryServiceUUIDs();
            LeScanCallbackProxy leScanCallbackProxy = new LeScanCallbackProxy();
//            leScanCallbackProxy.startLeScan(primaryServiceUUIDs.toArray(new UUID[primaryServiceUUIDs.size()]), this, deviceDefinitionInfo);
//        leScanCallbackProxy.startLeScan(this, deviceDefinitionInfo);
        leScanCallbackProxy.startLeScan(this, null);
            leScanCallbackProxies.add(leScanCallbackProxy);
//        }
        // TODO monitor the start results and if needed stop scanning....
        return true;
    }

    public void stopScanning(SHNDeviceScannerListener shnDeviceScannerListener) {
        for (LeScanCallbackProxy leScanCallbackProxy: leScanCallbackProxies) {
            leScanCallbackProxy.stopLeScan(this);
        }
        leScanCallbackProxies.clear();
    }

    void queueBleDeviceFoundInfoOnBleThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDeviceFoundEvent(bleDeviceFoundInfo);
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }

    boolean handleDeviceFoundEvent(BleDeviceFoundInfo bleDeviceFoundInfo) {

        if (!macAddressesOfFoundDevices.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            macAddressesOfFoundDevices.add(bleDeviceFoundInfo.getDeviceAddress());

            List<UUID> uuids = parseUUIDs(bleDeviceFoundInfo.scanRecord);
            for (UUID uuid: uuids) {
                for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: shnCentral.getRegisteredDeviceDefinitions()) {
                    if (shnDeviceDefinitionInfo.getPrimaryServiceUUIDs().contains(uuid)) {
                        final  SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(bleDeviceFoundInfo.bluetoothDevice, bleDeviceFoundInfo.rssi, bleDeviceFoundInfo.scanRecord, shnDeviceDefinitionInfo);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                shnDeviceScannerListener.deviceFound(SHNDeviceScanner.this, shnDeviceFoundInfo);
                            }
                        };
                        shnCentral.runOnHandlerThread(runnable);
                    }
                }
            }
        }

        return true; // Event handled
    }

    public void shutdown() {
    }

    // SHNDeviceScanner.LeScanCallback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (LOGGING) Log.e(TAG, "onLeScan");
        queueBleDeviceFoundInfoOnBleThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
    }

    private List<UUID> parseUUIDs(final byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        int offset = 0;
        while (offset < (advertisedData.length - 2)) {
            int len = advertisedData[offset++];
            if (len == 0)
                break;

            int type = advertisedData[offset++];
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (len > 1) {
                        int uuid16 = advertisedData[offset++];
                        uuid16 += (advertisedData[offset++] << 8);
                        len -= 2;
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Complete list of 128-bit UUIDs
                    // Loop through the advertised 128-bit UUID's.
                    while (len >= 16) {
                        try {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(advertisedData, offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            uuids.add(new UUID(leastSignificantBit, mostSignificantBit));
                        } catch (IndexOutOfBoundsException e) {
                            // TODO Do we want this -> Defensive programming.
                            Log.e(TAG, e.toString());
                            continue;
                        } finally {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            len -= 16;
                        }
                    }
                    break;
                default:
                    offset += (len - 1);
                    break;
            }
        }

        return uuids;
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
