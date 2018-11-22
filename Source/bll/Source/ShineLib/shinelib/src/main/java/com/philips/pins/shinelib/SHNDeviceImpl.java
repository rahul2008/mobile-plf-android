/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.statemachine.StateChangedListener;
import com.philips.pins.shinelib.statemachine.state.SHNDisconnectedState;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
@SuppressWarnings("FieldCanBeLocal")
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNService.CharacteristicDiscoveryListener {

    static final int GATT_ERROR = 0x0085;

    private static final String TAG = "SHNDeviceImpl";
    private SHNDeviceStateMachine stateMachine;
    private SHNDeviceResources sharedResources;

    @VisibleForTesting
    final SHNCentral.SHNCentralListener centralListener = new SHNCentral.SHNCentralListener() {
        @Override
        public void onStateUpdated(@NonNull final SHNCentral shnCentral, @NonNull final SHNCentral.State state) {
            stateMachine.getState().onStateUpdated(state);
        }
    };

    private StateChangedListener<SHNDeviceState> stateChangedListener = new StateChangedListener<SHNDeviceState>() {
        @Override
        public void onStateChanged(SHNDeviceState oldState, SHNDeviceState newState) {
            if (oldState == null) {
                return;
            }

            SHNLogger.i(TAG, String.format("State changed (%s -> %s)", oldState.getLogTag(), newState.getLogTag()));
            if (oldState.getExternalState() != newState.getExternalState()) {
                stateMachine.notifyStateToListener();
            }
        }
    };

    public enum SHNBondInitiator {
        NONE, PERIPHERAL, APP
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, int connectionPriority) {
        this(btDevice, shnCentral, deviceTypeName, SHNBondInitiator.NONE, connectionPriority);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName) {
        this(btDevice, shnCentral, deviceTypeName, SHNBondInitiator.NONE);
    }

    @Deprecated
    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, boolean deviceBondsDuringConnect) {
        this(btDevice, shnCentral, deviceTypeName, deviceBondsDuringConnect ? SHNBondInitiator.PERIPHERAL : SHNBondInitiator.NONE);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNBondInitiator shnBondInitiator) {
        this(btDevice, shnCentral, deviceTypeName, shnBondInitiator, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNBondInitiator shnBondInitiator, int connectionPriority) {
        this(btDevice, shnCentral, deviceTypeName, new SHNDeviceResources(btDevice, shnCentral, deviceTypeName, shnBondInitiator, connectionPriority));
    }

    @VisibleForTesting
    SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNDeviceResources resources) {
        sharedResources = resources;
        stateMachine = new SHNDeviceStateMachine(this, sharedResources);
        stateMachine.addStateListener(stateChangedListener);

        SHNDeviceState initialState = new SHNDisconnectedState(stateMachine);
        stateMachine.setState(initialState);

        shnCentral.addInternalListener(centralListener);

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
    public void refreshCache() {
        // TODO da thing
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateMachine.registerSHNDeviceListener(shnDeviceListener);
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        stateMachine.unregisterSHNDeviceListener();
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

    public void registerService(SHNService shnService) {
        sharedResources.registerService(shnService);
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }

    @Override
    public String toString() {
        return "SHNDevice - " + sharedResources.getName() + " [" + sharedResources.getBtDevice().getAddress() + "]";
    }
}
