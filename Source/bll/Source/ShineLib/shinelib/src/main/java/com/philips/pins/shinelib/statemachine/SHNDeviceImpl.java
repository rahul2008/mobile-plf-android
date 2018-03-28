/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

/*
@startuml
Disconnected: Disconnected
[*] --> Disconnected
Disconnected --> GattConnecting : connect /\nconnectGatt, restartConnectTimer, onStateChange(GattConnecting)
GattConnecting: Connecting
GattConnecting --> Disconnected : onConnectionStateChange(Disconnected) no timer or timer expired/\nclose, stopConnectTimer, onFailedToConnect, onStateChange(Disconnected)
GattConnecting --> GattConnecting : onConnectionStateChange(Disconnected) timer not expired/\nclose, connectGatt
GattConnecting --> Disconnecting : connectTimeout /\ndisconnect, onFailedToConnect, onStateChange(Disconnecting)
GattConnecting --> WaitingUntilBonded : onConnectionStateChange(Connected) & waitForBond /\nstopConnectTimer, startBondCreationTimer
WaitingUntilBonded: Connecting
WaitingUntilBonded --> DiscoveringServices : bondCreated | bondCreationTimeout /\ndiscoverServices, stopBondCreationTimer, restartConnectionTimer
GattConnecting --> DiscoveringServices : onConnectionStateChange(Connected) & !waitForBond /\ndiscoverServices, restartConnectionTimer
DiscoveringServices: Connecting
DiscoveringServices --> InitializingServices : onServicesDiscovered /\nconnectToBle, restartConnectionTimer
InitializingServices: Connecting
InitializingServices --> Ready : all services ready /\nstopConnectionTimer, onStateChange(Connected)
DiscoveringServices --> Disconnecting : connectTimeout /\ndisconnect, onFailedToConnect, onStateChange(Disconnecting)
InitializingServices --> Disconnecting : connectTimeout /\ndisconnect, disconnectFromBle, onFailedToConnect, onStateChange(Disconnecting)
Ready: Connected
Ready --> Disconnecting : disconnect /\ndisconnect, disconnectFromBle, onStateChange(Disconnecting)
Disconnecting --> Disconnected : onConnectionStateChange(Disconnected) /\nclose, onStateChange(Disconnected)
Disconnecting: Disconnecting
Ready --> Disconnected : onConnectionStateChange(Disconnected) /\nclose, onStateChange(Disconnected)
@enduml
 */

package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;

import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNBondStatusListener, SHNCentral.SHNCentralListener, SHNService.CharacteristicDiscoveryListener {

    private StateContext stateContext;

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

    @Override
    public void disconnect() {
        stateContext.getState().disconnect();
    }

    @Override
    public void readRSSI() {

    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {

    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {

    }

    @Override
    public void registerDiscoveryListener(DiscoveryListener discoveryListener) {

    }

    @Override
    public void unregisterDiscoveryListener(DiscoveryListener discoveryListener) {

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
}