/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;

import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNBondStatusListener, SHNCentral.SHNCentralListener, SHNService.CharacteristicDiscoveryListener {

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
        stateContext = new StateContext(btDevice, shnCentral, shnBondInitiator, this, this);
        DisconnectedState state = new DisconnectedState(stateContext);
        stateContext.setState(state);
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDeviceTypeName() {
        return null;
    }

    @Override
    public void connect() {
        stateContext.getState().connect();
    }

    @Override
    public void connect(long connectTimeOut) {
        stateContext.getState().connect(connectTimeOut);
    }

    void connect(final boolean withTimeout, final long timeoutInMS) {
        stateContext.getState().connect(withTimeout, timeoutInMS);
    }

    @Override
    public void disconnect() {
        stateContext.getState().disconnect();
    }

    @Override
    public void readRSSI() {

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
        return null;
    }

    @Override
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return null;
    }

    @Nullable
    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return null;
    }

    @Nullable
    @Override
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return null;
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

    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        stateContext.getState().onBondStatusChanged(device, bondState, previousBondState);
    }

    public void registerService(SHNService shnService) {
        stateContext.registerService(shnService);
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {
        }
    };
}
