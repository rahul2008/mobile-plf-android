package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 310188215 on 04/05/15.
 */
public class BTGatt extends BluetoothGattCallback {
    private static final String TAG = BTGatt.class.getSimpleName();
    private static final boolean LOGGING = false;

    public interface BTGattCallback
    {
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

    private final BTGattCallback btGattCallback;
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
    }

    public void disconnect() {
        bluetoothGatt.disconnect();
    }

    public void discoverServices() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt.discoverServices()) {
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

    public List<BluetoothGattService> getServices() {
        return bluetoothGatt.getServices();
    }

    public void readCharacteristic(final BluetoothGattCharacteristic characteristic) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt.readCharacteristic(characteristic)) {
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
                if (characteristic.setValue(data) && bluetoothGatt.writeCharacteristic(characteristic)) {
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
                if (bluetoothGatt.readDescriptor(descriptor)) {
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
                if (descriptor.setValue(data) && bluetoothGatt.writeDescriptor(descriptor)) {
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
        return bluetoothGatt.setCharacteristicNotification(characteristic, enable);
    }

    private void executeNextCommandIfAllowed() {
        if (!waitingForCompletion && !commandQueue.isEmpty()) {
            Runnable runnable = commandQueue.remove(0);
            runnable.run();
        }
    }

    // Implements BluetoothGattCallback
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, final int status, final int newState) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.w(TAG, "onConnectionStateChange status: " + status + " newState: " + newState);
                btGattCallback.onConnectionStateChange(BTGatt.this, status, newState);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.w(TAG, "onServicesDiscovered status: " + status);
                btGattCallback.onServicesDiscovered(BTGatt.this, status);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        byte[] data = characteristic.getValue();
        if (data != null) data = data.clone();
        final byte[] finalData = data;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.w(TAG, "onCharacteristicRead status: " + status);
                btGattCallback.onCharacteristicReadWithData(BTGatt.this, characteristic, status, finalData);
                waitingForCompletion = false;
                executeNextCommandIfAllowed();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.w(TAG, "onCharacteristicWrite status: " + status);
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
                if (LOGGING) Log.w(TAG, "onCharacteristicChanged");
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
                if (LOGGING) Log.w(TAG, "onDescriptorRead status: " + status);
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
                if (LOGGING) Log.w(TAG, "onDescriptorWrite status: " + status);
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
                if (LOGGING) Log.w(TAG, "onReliableWriteCompleted status: " + status);
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
                if (LOGGING) Log.w(TAG, "onReadRemoteRssi status: " + status);
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
                if (LOGGING) Log.w(TAG, "onMtuChanged status: " + status);
                btGattCallback.onMtuChanged(BTGatt.this, mtu, status);
            }
        };
        handler.post(runnable);
    }
}
