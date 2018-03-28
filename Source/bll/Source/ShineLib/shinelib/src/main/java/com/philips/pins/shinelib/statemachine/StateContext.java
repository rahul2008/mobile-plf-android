package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
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
    private final SHNDeviceImpl.SHNBondInitiator shnBondInitiator;

    private SHNDevice.SHNDeviceListener shnDeviceListener;
    private SHNDevice.DiscoveryListener discoveryListener;
    private Map<UUID, SHNService> registeredServices = new HashMap<>();

    StateContext(State initialState, BTDevice btDevice, SHNCentral shnCentral, SHNDeviceImpl.SHNBondInitiator shnBondInitiator) {
        this.state = initialState;
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
        shnService.registerSHNServiceListener(this);
        shnService.registerCharacteristicDiscoveryListener(this);
    }
}
