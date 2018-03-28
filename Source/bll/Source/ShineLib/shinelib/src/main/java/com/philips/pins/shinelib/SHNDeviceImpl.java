/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.*;

import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNCentralListener, SHNService.CharacteristicDiscoveryListener {

    public static final int GATT_ERROR = 0x0085;

    private StateMachine stateMachine;

    public enum SHNBondInitiator {
        NONE, PERIPHERAL, APP
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName) {
        this(btDevice, shnCentral, deviceTypeName, false);
    }

    @Deprecated
    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, boolean deviceBondsDuringConnect) {
        this(btDevice, shnCentral, deviceTypeName, deviceBondsDuringConnect ? SHNBondInitiator.PERIPHERAL : SHNBondInitiator.NONE);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNBondInitiator shnBondInitiator) {
        stateMachine = new StateMachine(this, btDevice, shnCentral, deviceTypeName, shnBondInitiator, this, btGattCallback);
    }

    @Override
    public State getState() {
        return stateMachine.getState().getExternalState();
    }

    @Override
    public String getAddress() {
        return stateMachine.getSharedResources().getBtDevice().getAddress();
    }

    @Override
    public String getName() {
        return stateMachine.getSharedResources().getName();
    }

    @Override
    public void setName(String name) {
        stateMachine.getSharedResources().setName(name);
    }

    @Override
    public String getDeviceTypeName() {
        return stateMachine.getSharedResources().getDeviceTypeName();
    }

    @Override
    public void connect() {
        stateMachine.getState().connect();
    }

    @Override
    public void connect(long connectTimeOut) {
        stateMachine.getState().connect(connectTimeOut);
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        stateMachine.getState().connect(withTimeout, timeoutInMS);
    }

    @Override
    public void disconnect() {
        stateMachine.getState().disconnect();
    }

    public boolean isBonded() {
        return stateMachine.getSharedResources().getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    @Override
    public void readRSSI() {
        BTGatt btGatt = stateMachine.getSharedResources().getBtGatt();
        if(btGatt != null) {
            btGatt.readRSSI();
        }
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateMachine.getSharedResources().registerSHNDeviceListener(shnDeviceListener);
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateMachine.getSharedResources().unregisterSHNDeviceListener();
    }

    @Override
    public void registerDiscoveryListener(DiscoveryListener discoveryListener) {
        stateMachine.getSharedResources().registerDiscoveryListener(discoveryListener);
    }

    @Override
    public void unregisterDiscoveryListener(DiscoveryListener discoveryListener) {
        stateMachine.getSharedResources().unregisterDiscoveryListener();
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return stateMachine.getSharedResources().getSupportedCapabilityTypes();
    }

    @Override
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return stateMachine.getSharedResources().getSupportedCapabilityClasses();
    }

    @Nullable
    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return stateMachine.getSharedResources().getCapabilityForType(type);
    }

    @Nullable
    @Override
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return stateMachine.getSharedResources().getCapability(type);
    }

    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType type) {
        stateMachine.getSharedResources().registerCapability(shnCapability, type);
    }

    public <T extends SHNCapability> void registerCapability(@NonNull final Class<? extends SHNCapability> type, @NonNull final T capability) {
        stateMachine.getSharedResources().registerCapability(type, capability);
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        stateMachine.getState().onServiceStateChanged(shnService, state);
    }

    @Override
    public void onCharacteristicDiscovered(@NonNull UUID characteristicUuid, byte[] data, @Nullable SHNCharacteristic characteristic) {
        stateMachine.getState().onCharacteristicDiscovered(characteristicUuid, data, characteristic);

    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        stateMachine.getState().onStateUpdated(shnCentral);
    }

    public void registerService(SHNService shnService) {
        stateMachine.getSharedResources().registerService(shnService);
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }

    @Override
    public String toString() {
        return "SHNDevice - " + stateMachine.getSharedResources().getName() + " [" + stateMachine.getSharedResources().getBtDevice().getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            stateMachine.getState().onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            stateMachine.getState().onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = stateMachine.getSharedResources().getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = stateMachine.getSharedResources().getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = stateMachine.getSharedResources().getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = stateMachine.getSharedResources().getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = stateMachine.getSharedResources().getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            stateMachine.getSharedResources().getDeviceListener().onReadRSSI(rssi);
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {

        }
    };
}
