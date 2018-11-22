/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityThreadSafe;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNDeviceResources {

    private BTGatt btGatt;
    private final BTDevice btDevice;
    private final String deviceTypeName;
    private String name;

    private int connectionPriority;
    private final SHNCentral shnCentral;
    private final SHNDeviceImpl.SHNBondInitiator shnBondInitiator;
    private long lastDisconnectedTimeMillis;

    private SHNDevice.DiscoveryListener discoveryListener;
    private Map<UUID, SHNService> registeredServices = new HashMap<>();
    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Map<Class<? extends SHNCapability>, SHNCapability> registeredByClassCapabilities = new HashMap<>();

    public SHNDeviceResources(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNDeviceImpl.SHNBondInitiator shnBondInitiator, int connectionPriority) {
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.deviceTypeName = deviceTypeName;
        this.shnBondInitiator = shnBondInitiator;
        this.connectionPriority = connectionPriority < BluetoothGatt.CONNECTION_PRIORITY_BALANCED || connectionPriority > BluetoothGatt.CONNECTION_PRIORITY_LOW_POWER ? BluetoothGatt.CONNECTION_PRIORITY_BALANCED : connectionPriority;
        this.name = btDevice.getName();
    }

    public BTDevice getBtDevice() {
        return btDevice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public SHNCentral getShnCentral() {
        return shnCentral;
    }

    public SHNDeviceImpl.SHNBondInitiator getShnBondInitiator() {
        return shnBondInitiator;
    }

    @Nullable
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

    @Nullable
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return registeredCapabilities.keySet();
    }

    @Nullable
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return registeredByClassCapabilities.keySet();
    }

    @Nullable
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return registeredCapabilities.get(type);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return (T) registeredByClassCapabilities.get(type);
    }

    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType type) {
        if (registeredCapabilities.containsKey(type)) {
            throw new IllegalStateException("Capability already registered");
        }

        SHNCapability shnCapabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(shnCapability, type, shnCentral.getInternalHandler(), shnCentral.getUserHandler());
        registeredCapabilities.put(type, shnCapabilityWrapper);
        registerCapability(shnCapability.getClass(), shnCapabilityWrapper);

        SHNCapabilityType counterPart = SHNCapabilityType.getCounterPart(type);
        if (counterPart != null) {
            registeredCapabilities.put(counterPart, shnCapabilityWrapper);
        }
    }

    public <C extends SHNCapability> void registerCapability(@NonNull final Class<C> type, @NonNull final SHNCapability capability) {
        if (registeredByClassCapabilities.containsKey(type)) {
            throw new IllegalStateException("Capability already registered");
        }

        if (capability instanceof SHNCapabilityThreadSafe) {
            ((SHNCapabilityThreadSafe) capability).setHandlers(shnCentral.getInternalHandler(), shnCentral.getUserHandler());
        }

        registeredByClassCapabilities.put(type, capability);
    }

    public void registerDiscoveryListener(final SHNDevice.DiscoveryListener discoveryListener) {
        this.discoveryListener = discoveryListener;
    }

    public void unregisterDiscoveryListener() {
        this.discoveryListener = null;
    }

    @Nullable
    public SHNDevice.DiscoveryListener getDiscoveryListener() {
        return this.discoveryListener;
    }

    public void setLastDisconnectedTimeMillis(long lastDisconnectedTimeMillis) {
        this.lastDisconnectedTimeMillis = lastDisconnectedTimeMillis;
    }

    public long getLastDisconnectedTimeMillis() {
        return lastDisconnectedTimeMillis;
    }

    public int getConnectionPriority() {
        return connectionPriority;
    }
}
