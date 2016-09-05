/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.LinkedList;
import java.util.List;

public class BTGatt extends BluetoothGattCallback {
    private static final String TAG = BTGatt.class.getSimpleName();
    private static final boolean ENABLE_DEBUG_LOGGING = false;
    private Runnable currentCommand;

    public interface BTGattCallback {
        void onConnectionStateChange(BTGatt gatt, int status, int newState);

        void onServicesDiscovered(BTGatt gatt, int status);

        void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data);

        void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status);

        void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data);

        void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data);

        void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status);

        void onReliableWriteCompleted(BTGatt gatt, int status);

        void onReadRemoteRssi(BTGatt gatt, int rssi, int status);

        void onMtuChanged(BTGatt gatt, int mtu, int status);
    }

    class NullBTGattCallback implements BTGattCallback {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {}

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {}

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {}

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {}

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {}

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {}

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {}

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {}

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {}

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {}
    }

    private BTGattCallback btGattCallback;
    private final Handler handler;
    private BluetoothGatt bluetoothGatt;
    private List<Runnable> commandQueue;
    private boolean waitingForCompletion;

    BTGatt(BTGattCallback btGattCallback, Handler handler) {
        this.btGattCallback = btGattCallback;
        this.handler = handler;
        commandQueue = new LinkedList<>();
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public void close() {
        bluetoothGatt.close();
        bluetoothGatt = null;
        commandQueue.clear();
        btGattCallback = new NullBTGattCallback();
    }

    public void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public void discoverServices() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt != null && bluetoothGatt.discoverServices()) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onServicesDiscovered(BTGatt.this, BluetoothGatt.GATT_FAILURE);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public void readRSSI() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt != null && bluetoothGatt.readRemoteRssi()) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onReadRemoteRssi(BTGatt.this, 0, BluetoothGatt.GATT_FAILURE);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public List<BluetoothGattService> getServices() {
        return bluetoothGatt == null ? null : bluetoothGatt.getServices();
    }

    public void readCharacteristic(final BluetoothGattCharacteristic characteristic) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt != null && bluetoothGatt.readCharacteristic(characteristic)) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onCharacteristicReadWithData(BTGatt.this, characteristic, BluetoothGatt.GATT_FAILURE, null);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public void writeCharacteristic(final BluetoothGattCharacteristic characteristic, final byte[] data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (characteristic.setValue(data) && bluetoothGatt != null && bluetoothGatt.writeCharacteristic(characteristic)) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onCharacteristicWrite(BTGatt.this, characteristic, BluetoothGatt.GATT_FAILURE);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public void readDescriptor(final BluetoothGattDescriptor descriptor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt != null && bluetoothGatt.readDescriptor(descriptor)) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onDescriptorReadWithData(BTGatt.this, descriptor, BluetoothGatt.GATT_FAILURE, null);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public void writeDescriptor(final BluetoothGattDescriptor descriptor, final byte[] data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (descriptor.setValue(data) && bluetoothGatt != null &&  bluetoothGatt.writeDescriptor(descriptor)) {
                    waitingForCompletion = true;
                } else {
                    btGattCallback.onDescriptorWrite(BTGatt.this, descriptor, BluetoothGatt.GATT_FAILURE);
                    executeNextCommandIfAllowed();
                }
            }
        };
        commandQueue.add(runnable);
        executeNextCommandIfAllowed();
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        return bluetoothGatt != null && bluetoothGatt.setCharacteristicNotification(characteristic, enable);
    }

    private void executeNextCommandIfAllowed() {
        if (!waitingForCompletion && !commandQueue.isEmpty()) {
            currentCommand = commandQueue.remove(0);
            currentCommand.run();
        }
    }

    // Implements BluetoothGattCallback
    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onConnectionStateChange status: " + status + " newState: " + newState);
                btGattCallback.onConnectionStateChange(BTGatt.this, status, newState);
// For Tuscany we found that Android does not negotiate the MTU size. The below statements work for Lolipop on a MOTOG.
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    boolean result = gatt.requestMtu(128);
//                    Log.e(TAG, "gatt.requestMtu(128); = " + result);
//                }
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onServicesDiscovered status: " + status);
                btGattCallback.onServicesDiscovered(BTGatt.this, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        if (retryCurrentCommandWhenStatusIndicatesAnError(status)) {
            return;
        }

        byte[] data = characteristic.getValue();
        if (data != null) data = data.clone();
        final byte[] finalData = data;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onCharacteristicRead status: " + status);
                btGattCallback.onCharacteristicReadWithData(BTGatt.this, characteristic, status, finalData);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    private boolean retryCurrentCommandWhenStatusIndicatesAnError(int status) {
        if (status != BluetoothGatt.GATT_SUCCESS && currentCommand != null) {
            handler.post(new Runnable() {
                private Runnable retryCommandRunnable = currentCommand;
                @Override
                public void run() {
                    waitingForCompletion = false;
                    retryCommandRunnable.run();
                }
            });
            currentCommand = null;
            return true;
        }
        return false;
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onCharacteristicWrite status: " + status);
                btGattCallback.onCharacteristicWrite(BTGatt.this, characteristic, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        final byte[] data = characteristic.getValue().clone();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onCharacteristicChanged");
                btGattCallback.onCharacteristicChangedWithData(BTGatt.this, characteristic, data);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        final byte[] data = descriptor.getValue().clone();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onDescriptorRead status: " + status);
                btGattCallback.onDescriptorReadWithData(BTGatt.this, descriptor, status, data);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onDescriptorWrite status: " + status);
                btGattCallback.onDescriptorWrite(BTGatt.this, descriptor, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onReliableWriteCompleted status: " + status);
                btGattCallback.onReliableWriteCompleted(BTGatt.this, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onReadRemoteRssi status: " + status);
                btGattCallback.onReadRemoteRssi(BTGatt.this, rssi, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, final int mtu, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DebugLog("onMtuChanged status: " + status);
                btGattCallback.onMtuChanged(BTGatt.this, mtu, status);
            }
        };
        handler.post(runnable);
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            SHNLogger.i(TAG, log);
        }
    }
}
