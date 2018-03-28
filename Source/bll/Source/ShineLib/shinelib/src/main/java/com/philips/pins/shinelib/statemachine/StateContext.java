package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateContext {
    private State state;
    private BTGatt btGatt;
    private final BTDevice btDevice;
    private final SHNCentral shnCentral;
    private final com.philips.pins.shinelib.statemachine.SHNDeviceImpl.SHNBondInitiator shnBondInitiator;
    private BTGatt.BTGattCallback btGattCallback;

    private SHNDevice.SHNDeviceListener shnDeviceListener;
    private SHNDevice.DiscoveryListener discoveryListener;
    private Map<UUID, SHNService> registeredServices = new HashMap<>();

    StateContext(BTDevice btDevice, SHNCentral shnCentral, com.philips.pins.shinelib.statemachine.SHNDeviceImpl.SHNBondInitiator shnBondInitiator) {
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.shnBondInitiator = shnBondInitiator;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public BTDevice getBtDevice() {
        return btDevice;
    }

    public SHNCentral getShnCentral() {
        return shnCentral;
    }

    public SHNDeviceImpl.SHNBondInitiator getShnBondInitiator() {
        return shnBondInitiator;
    }

    public BTGatt getBtGatt() {
        return btGatt;
    }

    public void setBtGatt(BTGatt btGatt) {
        this.btGatt = btGatt;
    }

    public void registerService(SHNService shnService) {
        registeredServices.put(shnService.getUuid(), shnService);
    }

    public SHNService getSHNService(UUID serviceUUID) {
        return registeredServices.get(serviceUUID);
    }

    public Map<UUID, SHNService> getSHNServices() {
        return registeredServices;
    }

    public void registerSHNDeviceListener(SHNDevice.SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
    }

    public void unregisterSHNDeviceListener() {
        this.shnDeviceListener = null;
    }

    public SHNDevice.SHNDeviceListener getDeviceListener() {
        return this.shnDeviceListener;
    }

    public void registerDiscoveryListener(final SHNDevice.DiscoveryListener discoveryListener) {
        this.discoveryListener = discoveryListener;
    }

    public void unregisterDiscoveryListener() {
        this.discoveryListener = null;
    }

    public SHNDevice.DiscoveryListener getDiscoveryListener() {
        return this.discoveryListener;
    }

    public BTGatt.BTGattCallback getBTGattCallback() {
        return btGattCallback;
    }

    public void setBTGattCallback(BTGatt.BTGattCallback btGattCallback) {
        this.btGattCallback = btGattCallback;
    }

}
