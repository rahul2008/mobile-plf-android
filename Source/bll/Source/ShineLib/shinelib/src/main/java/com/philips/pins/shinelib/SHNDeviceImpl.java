/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.*;
import com.philips.pins.shinelib.statemachine.state.SHNDisconnectedState;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNCentralListener, SHNService.CharacteristicDiscoveryListener {

    public static final int GATT_ERROR = 0x0085;

    private static final String TAG = "SHNDeviceImpl";
    private SHNDeviceStateMachine stateMachine;
    private SHNDeviceResources sharedResources;

    private StateChangedListener<SHNDeviceState> stateStateChangedListener = new StateChangedListener<SHNDeviceState>() {
        @Override
        public void onStateChanged(SHNDeviceState oldState, SHNDeviceState newState) {
            if (oldState == null) {
                return;
            }

            SHNLogger.i(TAG, String.format("State changed (%s -> %s)", oldState.getClass().getSimpleName(), newState.getClass().getSimpleName()));
            if (oldState.getExternalState() != newState.getExternalState()) {
                sharedResources.notifyStateToListener();
            }
        }
    };

    public enum SHNBondInitiator {
        NONE, PERIPHERAL, APP
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName) {
        this(btDevice, shnCentral, deviceTypeName, SHNBondInitiator.NONE);
    }

    @Deprecated
    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, boolean deviceBondsDuringConnect) {
        this(btDevice, shnCentral, deviceTypeName, deviceBondsDuringConnect ? SHNBondInitiator.PERIPHERAL : SHNBondInitiator.NONE);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNBondInitiator shnBondInitiator) {
        sharedResources = new SHNDeviceResources(this, btDevice, shnCentral, deviceTypeName, shnBondInitiator, this, btGattCallback);
        stateMachine = new SHNDeviceStateMachine(sharedResources);
        stateMachine.addStateListener(stateStateChangedListener);

        SHNDeviceState initialState = new SHNDisconnectedState(stateMachine);
        stateMachine.setState(initialState);

        shnCentral.registerShnCentralListener(this);

        SHNLogger.i(TAG, "Created new instance of SHNDevice for type: " + deviceTypeName + " address: " + btDevice.getAddress());
    }

    @Override
    public State getState() {
        return stateMachine.getState().getExternalState();
    }

    @Override
    public String getAddress() {
        return sharedResources.getBtDevice().getAddress();
    }

    @Override
    public String getName() {
        return sharedResources.getName();
    }

    @Override
    public void setName(String name) {
        sharedResources.setName(name);
    }

    @Override
    public String getDeviceTypeName() {
        return sharedResources.getDeviceTypeName();
    }

    @Override
    public void connect() {
        stateMachine.getState().connect();
    }

    @Override
    public void connect(long connectTimeOut) {
        stateMachine.getState().connect(connectTimeOut);
    }

    @Override
    public void disconnect() {
        stateMachine.getState().disconnect();
    }

    public boolean isBonded() {
        return sharedResources.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    @Override
    public void readRSSI() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.readRSSI();
        }
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        sharedResources.registerSHNDeviceListener(shnDeviceListener);
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        sharedResources.unregisterSHNDeviceListener();
    }

    @Override
    public void registerDiscoveryListener(DiscoveryListener discoveryListener) {
        sharedResources.registerDiscoveryListener(discoveryListener);
    }

    @Override
    public void unregisterDiscoveryListener(DiscoveryListener discoveryListener) {
        sharedResources.unregisterDiscoveryListener();
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return sharedResources.getSupportedCapabilityTypes();
    }

    @Override
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return sharedResources.getSupportedCapabilityClasses();
    }

    @Nullable
    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return sharedResources.getCapabilityForType(type);
    }

    @Nullable
    @Override
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return sharedResources.getCapability(type);
    }

    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType type) {
        sharedResources.registerCapability(shnCapability, type);
    }

    public <T extends SHNCapability> void registerCapability(@NonNull final Class<? extends SHNCapability> type, @NonNull final T capability) {
        sharedResources.registerCapability(type, capability);
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        stateMachine.getState().onServiceStateChanged(shnService, state);
    }

    @Override
    public void onCharacteristicDiscovered(@NonNull UUID characteristicUuid, byte[] data, @Nullable SHNCharacteristic characteristic) {
        SHNDevice.DiscoveryListener discoveryListener = sharedResources.getDiscoveryListener();
        if (discoveryListener != null) {
            discoveryListener.onCharacteristicDiscovered(characteristicUuid, data, characteristic);
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        stateMachine.getState().onStateUpdated(shnCentral);
    }

    public void registerService(SHNService shnService) {
        sharedResources.registerService(shnService);
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }

    @Override
    public String toString() {
        return "SHNDevice - " + sharedResources.getName() + " [" + sharedResources.getBtDevice().getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            SHNLogger.i(TAG, "BTGattCallback - onConnectionStateChange (newState = '" + bluetoothStateToString(newState) + "', status = " + status + ")");
            stateMachine.getState().onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            stateMachine.getState().onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = sharedResources.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            SHNDevice.SHNDeviceListener deviceListener = sharedResources.getDeviceListener();
            if (deviceListener != null) {
                deviceListener.onReadRSSI(rssi);
            }
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {

        }
    };

    private static String bluetoothStateToString(int bluetoothState) {
        return (bluetoothState == BluetoothProfile.STATE_CONNECTED) ? "Connected" :
                (bluetoothState == BluetoothProfile.STATE_CONNECTING) ? "Connecting" :
                        (bluetoothState == BluetoothProfile.STATE_DISCONNECTED) ? "Disconnected" :
                                (bluetoothState == BluetoothProfile.STATE_DISCONNECTING) ? "Disconnecting" : "Unknown";
    }
}
