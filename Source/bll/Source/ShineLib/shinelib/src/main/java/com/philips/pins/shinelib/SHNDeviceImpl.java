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

    private StateContext stateContext;

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
        stateContext = new StateContext(this, btDevice, shnCentral, deviceTypeName, shnBondInitiator, this, btGattCallback);
        DisconnectedState state = new DisconnectedState(stateContext);
        stateContext.setState(state);
    }

    @Override
    public State getState() {
        return stateContext.getState().getExternalState();
    }

    @Override
    public String getAddress() {
        return stateContext.getBtDevice().getAddress();
    }

    @Override
    public String getName() {
        return stateContext.getName();
    }

    @Override
    public void setName(String name) {
        stateContext.setName(name);
    }

    @Override
    public String getDeviceTypeName() {
        return stateContext.getDeviceTypeName();
    }

    @Override
    public void connect() {
        stateContext.getState().connect();
    }

    @Override
    public void connect(long connectTimeOut) {
        stateContext.getState().connect(connectTimeOut);
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        stateContext.getState().connect(withTimeout, timeoutInMS);
    }

    @Override
    public void disconnect() {
        stateContext.getState().disconnect();
    }

    public boolean isBonded() {
        return stateContext.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    @Override
    public void readRSSI() {
        BTGatt btGatt = stateContext.getBtGatt();
        if(btGatt != null) {
            btGatt.readRSSI();
        }
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateContext.registerSHNDeviceListener(shnDeviceListener);
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateContext.unregisterSHNDeviceListener();
    }

    @Override
    public void registerDiscoveryListener(DiscoveryListener discoveryListener) {
        stateContext.registerDiscoveryListener(discoveryListener);
    }

    @Override
    public void unregisterDiscoveryListener(DiscoveryListener discoveryListener) {
        stateContext.unregisterDiscoveryListener();
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return stateContext.getSupportedCapabilityTypes();
    }

    @Override
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return stateContext.getSupportedCapabilityClasses();
    }

    @Nullable
    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return stateContext.getCapabilityForType(type);
    }

    @Nullable
    @Override
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return stateContext.getCapability(type);
    }

    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType type) {
        stateContext.registerCapability(shnCapability, type);
    }

    public <T extends SHNCapability> void registerCapability(@NonNull final Class<? extends SHNCapability> type, @NonNull final T capability) {
        stateContext.registerCapability(type, capability);
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        stateContext.getState().onServiceStateChanged(shnService, state);
    }

    @Override
    public void onCharacteristicDiscovered(@NonNull UUID characteristicUuid, byte[] data, @Nullable SHNCharacteristic characteristic) {
        stateContext.getState().onCharacteristicDiscovered(characteristicUuid, data, characteristic);

    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        stateContext.getState().onStateUpdated(shnCentral);
    }

    public void registerService(SHNService shnService) {
        stateContext.registerService(shnService);
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }

    @Override
    public String toString() {
        return "SHNDevice - " + stateContext.getName() + " [" + stateContext.getBtDevice().getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            stateContext.getState().onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            stateContext.getState().onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = stateContext.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = stateContext.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = stateContext.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = stateContext.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = stateContext.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            stateContext.getDeviceListener().onReadRSSI(rssi);
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {

        }
    };
}
