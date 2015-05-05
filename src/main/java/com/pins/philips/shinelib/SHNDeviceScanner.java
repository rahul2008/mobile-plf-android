package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;

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
    private LeScanCallbackProxy leScanCallbackProxy;
    private SHNDeviceScannerListener shnDeviceScannerListener;
    private boolean scanning = false;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable scanningTimer;
    private final SHNCentral shnCentral;

    public interface SHNDeviceScannerListener {
        void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo);
        void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    public SHNDeviceScanner(SHNCentral shnCentral, List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(SHNDeviceScannerListener shnDeviceScannerListener, long stopScanningAfterMS) {
        if (scanning) {
            return false;
        }
        macAddressesOfFoundDevices.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
        leScanCallbackProxy = new LeScanCallbackProxy();
        scanning = leScanCallbackProxy.startLeScan(this, null);

        scanningTimer = new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        };
        shnCentral.getInternalHandler().postDelayed(scanningTimer, stopScanningAfterMS);

        return true;
    }

    public void stopScanning() {
        if (scanning) {
            shnCentral.getInternalHandler().removeCallbacks(scanningTimer);
            scanningTimer = null;
            leScanCallbackProxy.stopLeScan(this);
            leScanCallbackProxy = null;
            shnDeviceScannerListener.scanStopped(this);
            shnDeviceScannerListener = null;
            scanning = false;
        }
    }

    private void postBleDeviceFoundInfoOnBleThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDeviceFoundEvent(bleDeviceFoundInfo);
            }
        };
        shnCentral.getInternalHandler().post(runnable);
    }

    private void handleDeviceFoundEvent(BleDeviceFoundInfo bleDeviceFoundInfo) {

        if (!macAddressesOfFoundDevices.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            macAddressesOfFoundDevices.add(bleDeviceFoundInfo.getDeviceAddress());

            List<UUID> UUIDs = parseUUIDs(bleDeviceFoundInfo.scanRecord);
            for (UUID uuid: UUIDs) {
                for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: registeredDeviceDefinitions) {
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
    }

    public void shutdown() {
        stopScanning();
        // TODO What else: release references???
    }

    // SHNDeviceScanner.LeScanCallback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        postBleDeviceFoundInfoOnBleThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
    }

    private List<UUID> parseUUIDs(final byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<>();

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
