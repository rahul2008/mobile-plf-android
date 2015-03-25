package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.pins.philips.shinelib.framework.BleDeviceFoundInfo;
import com.pins.philips.shinelib.framework.SingleThreadEventDispatcher;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDevice {
    private static final String TAG = SHNDevice.class.getSimpleName();

    enum EventSubType {
        ServicesDiscovered, CharacteristicRead, CharacteristicWrite, CharacteristicChanged, DescriptorRead, DescriptorWrite, ReliableWriteCompleted, ReadRemoteRssi, MtuChanged, ConnectionStateChange
    }
    private enum RequestType {
        Connect, Disconnect
    }

    static class BleGattEvent extends SingleThreadEventDispatcher.BaseEvent {
        public final EventSubType eventSubType;
        public int status = Integer.MIN_VALUE;
        public int newState = Integer.MIN_VALUE;
        public int rssi = Integer.MIN_VALUE;
        public int mtu = Integer.MIN_VALUE;
        public BluetoothGattCharacteristic characteristic;
        public BluetoothGattDescriptor descriptor;

        public BleGattEvent(EventSubType eventSubType) {
            this.eventSubType = eventSubType;
        }
    }
    static class BleRequestEvent extends SingleThreadEventDispatcher.BaseEvent {
        public final RequestType requestType;

        public BleRequestEvent(RequestType requestType) {
            this.requestType = requestType;
        }
    }

    private final BleDeviceFoundInfo bleDeviceFoundInfo;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;
    private SingleThreadEventDispatcher.EventHandler bleGattEventHandler;
    private SingleThreadEventDispatcher.EventHandler bleGattConnectRequestHandler;

    public SHNDeviceState getState() {
        return shnDeviceState;
    }

    public enum SHNDeviceState {
        SHNDeviceStateDisconnected, SHNDeviceStateDisconnecting, SHNDeviceStateConnecting, SHNDeviceStateConnected
    }
    public interface SHNDeviceListener {
        public void onStateUpdated(SHNDevice shnDevice);
    }

    public SHNDevice(BleDeviceFoundInfo bleDeviceFoundInfo, final SHNCentral shnCentral) {
        this.bleDeviceFoundInfo = bleDeviceFoundInfo;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();
        bleGattEventHandler = new SingleThreadEventDispatcher.EventHandler() {
            @Override
            public boolean handleEvent(SingleThreadEventDispatcher.BaseEvent event) {
                return handleEventSub(event);
            }
        };
        bleGattConnectRequestHandler = new SingleThreadEventDispatcher.EventHandler() {
            @Override
            public boolean handleEvent(SingleThreadEventDispatcher.BaseEvent event) {
                return handleEventSub(event);
            }
        };

        this.bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                BleGattEvent event = new BleGattEvent(EventSubType.ConnectionStateChange);
                event.status = status;
                event.newState = newState;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.ServicesDiscovered);
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.CharacteristicRead);
                event.characteristic = characteristic;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.CharacteristicWrite);
                event.characteristic = characteristic;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                BleGattEvent event = new BleGattEvent(EventSubType.CharacteristicChanged);
                event.characteristic = characteristic;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.DescriptorRead);
                event.descriptor = descriptor;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.DescriptorWrite);
                event.descriptor = descriptor;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.ReliableWriteCompleted);
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.ReadRemoteRssi);
                event.rssi = rssi;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                BleGattEvent event = new BleGattEvent(EventSubType.MtuChanged);
                event.mtu = mtu;
                event.status = status;
                shnCentral.getEventDispatcher().queueEvent(event);
            }
        };
    }

    public boolean handleEventSub(SingleThreadEventDispatcher.BaseEvent event) {
        if (event instanceof BleGattEvent) {
            BleGattEvent bleGattEvent = (BleGattEvent) event;
            Log.e(TAG, "handleEvent: " + bleGattEvent.eventSubType.toString());
            switch (bleGattEvent.eventSubType) {
                case ConnectionStateChange:
                    handleGattConnectionStateChange(bleGattEvent.status, bleGattEvent.newState);
                    break;
                case ServicesDiscovered:
                    break;
                case CharacteristicRead:
                    break;
                case CharacteristicWrite:
                    break;
                case CharacteristicChanged:
                    break;
                case DescriptorRead:
                    break;
                case DescriptorWrite:
                    break;
                case ReliableWriteCompleted:
                    break;
                case ReadRemoteRssi:
                    break;
                case MtuChanged:
                    break;
            }
        } else if (event instanceof BleRequestEvent) {
            switch (((BleRequestEvent)event).requestType) {
                case Connect:
                    handleConnect();
                    break;
                case Disconnect:
                    handleDisconnect();
                    break;
            }
        }
        return true;
    }

    private void handleGattConnectionStateChange(int status, int newState) {
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
        }
        Log.e(TAG, "handleGattConnectionStateChange status: " + status + " newState: " + newState + " [" + shnDeviceState.name() + "]");
        updateShnDeviceState(shnDeviceState);
    }

    private SHNDeviceListener shnDeviceListener;
    private SHNDeviceState shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
    private String name;
    private String type;
    private UUID identifier;
    private int rssiWhenDiscovered; // How is that usefull?

    public String getAddress() {
        return bleDeviceFoundInfo.getDeviceAddress();
    }
    public String getName() {
        return bleDeviceFoundInfo.bluetoothDevice.getName();
    }


    public void connect()  {
        shnCentral.getEventDispatcher().register(bleGattEventHandler, BleGattEvent.class);
        shnCentral.getEventDispatcher().register(bleGattConnectRequestHandler, BleRequestEvent.class);
        BleRequestEvent event = new BleRequestEvent(RequestType.Connect);
        shnCentral.getEventDispatcher().queueEvent(event);
    }

    private void handleConnect()  {
        updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
        bluetoothGatt = bleDeviceFoundInfo.bluetoothDevice.connectGatt(applicationContext, false, bluetoothGattCallback);
    }

    public void disconnect() {
        BleRequestEvent event = new BleRequestEvent(RequestType.Disconnect);
        shnCentral.getEventDispatcher().queueEvent(event);
    }

    private void handleDisconnect() {
        bluetoothGatt.disconnect();
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
        return bleDeviceFoundInfo.bluetoothDevice.getName() + " [" + bleDeviceFoundInfo.bluetoothDevice.getAddress() + "]";
    }
}
