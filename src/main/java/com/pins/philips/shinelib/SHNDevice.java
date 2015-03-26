package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.pins.philips.shinelib.framework.BleDeviceFoundInfo;
import com.pins.philips.shinelib.framework.BluetoothGattCallbackOnExecutor;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDevice {
    private static final String TAG = SHNDevice.class.getSimpleName();
    private final BluetoothDevice bluetoothDevice;

    enum EventSubType {
        ServicesDiscovered, CharacteristicRead, CharacteristicWrite, CharacteristicChanged, DescriptorRead, DescriptorWrite, ReliableWriteCompleted, ReadRemoteRssi, MtuChanged, ConnectionStateChange
    }
    private enum RequestType {
        Connect, Disconnect
    }

    private final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;

    public SHNDeviceState getState() {
        return shnDeviceState;
    }

    public enum SHNDeviceState {
        SHNDeviceStateDisconnected, SHNDeviceStateDisconnecting, SHNDeviceStateConnecting, SHNDeviceStateConnected
    }
    public interface SHNDeviceListener {
        public void onStateUpdated(SHNDevice shnDevice);
    }

    public SHNDevice(BluetoothDevice bluetoothDevice, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, final SHNCentral shnCentral) {
        this.bluetoothDevice = bluetoothDevice;
        this.shnDeviceDefinitionInfo = shnDeviceDefinitionInfo;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();

        this.bluetoothGattCallback = new BluetoothGattCallbackOnExecutor(shnCentral.getScheduledThreadPoolExecutor(),new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                SHNDeviceState shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    if (bluetoothGatt != null) {
                        bluetoothGatt.close();
                        bluetoothGatt = null;
                    }
                    shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
                }
                else if (newState == BluetoothProfile.STATE_CONNECTED) {
                    shnDeviceState = SHNDeviceState.SHNDeviceStateConnected;
                    gatt.discoverServices();
                }
                Log.e(TAG, "onConnectionStateChange status: " + status + " newState: " + newState + " [" + shnDeviceState.name() + "]");
                updateShnDeviceState(shnDeviceState);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.e(TAG, "onServicesDiscovered status: " + status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    for (BluetoothGattService service: gatt.getServices()) {
                        Log.e(TAG, "onServicesDiscovered service.UUID: " + service.getUuid());
                        for (BluetoothGattCharacteristic characteristic: service.getCharacteristics()) {
                            Log.e(TAG, "onServicesDiscovered characteristic.UUID: " + characteristic.getUuid());
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            }
        });
    }

    private SHNDeviceListener shnDeviceListener;
    private SHNDeviceState shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
    private String name;
    private String type;
    private UUID identifier;
    private int rssiWhenDiscovered; // How is that usefull?

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }
    public String getName() {
        return bluetoothDevice.getName();
    }

    public void connect()  {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleConnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleConnect()  {
        updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
        bluetoothGatt = bluetoothDevice.connectGatt(applicationContext, false, bluetoothGattCallback);
    }

    public void disconnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDisconnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleDisconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private void updateShnDeviceState(SHNDeviceState newShnDeviceState) {
        shnDeviceState = newShnDeviceState;
        informListeners();
    }

    private void informListeners() {
        if (shnDeviceListener != null) {
            shnCentral.reportSHNDeviceUpdated(shnDeviceListener, this);
        }
    }

    public void setShnDeviceListener(SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
        informListeners();
    }

    public Set<SHNCapabilityType> getSupportedCapabilityTypes() { throw new UnsupportedOperationException(); }
    public SHNCapability getCapabilityForType(SHNCapabilityType type) { throw new UnsupportedOperationException(); }

    @Override
    public String toString() {
        return bluetoothDevice.getName() + " [" + bluetoothDevice.getAddress() + "]";
    }
}
