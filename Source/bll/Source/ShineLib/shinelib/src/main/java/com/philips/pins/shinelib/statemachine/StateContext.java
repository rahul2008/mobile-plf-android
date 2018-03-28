package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityThreadSafe;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StateContext {

    private static final String TAG = StateContext.class.getName();

    private State state;
    private final SHNDevice parent;
    private BTGatt btGatt;
    private final BTDevice btDevice;
    private final String deviceTypeName;
    private String name;
    private final SHNCentral shnCentral;
    private final SHNDeviceImpl.SHNBondInitiator shnBondInitiator;
    private final SHNCentral.SHNCentralListener shnCentralListener;
    private final BTGatt.BTGattCallback btGattCallback;
    private long lastDisconnectedTimeMillis;

    private SHNDevice.SHNDeviceListener shnDeviceListener;
    private SHNDevice.DiscoveryListener discoveryListener;
    private Map<UUID, SHNService> registeredServices = new HashMap<>();
    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Map<Class<? extends SHNCapability>, SHNCapability> registeredByClassCapabilities = new HashMap<>();

    public StateContext(SHNDevice parent, BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNDeviceImpl.SHNBondInitiator shnBondInitiator, SHNCentral.SHNCentralListener shnCentralListener, BTGatt.BTGattCallback btGattCallback) {
        this.parent = parent;
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.deviceTypeName = deviceTypeName;
        this.shnBondInitiator = shnBondInitiator;
        this.shnCentralListener = shnCentralListener;
        this.btGattCallback = btGattCallback;
        this.name = btDevice.getName();
    }

    public SHNCentral.SHNCentralListener getShnCentralListener() {
        return shnCentralListener;
    }

    public void setState(State state) {
        SHNLogger.e(TAG, String.format("State changed to %s", state.getClass().getName()));

        this.state = state;
        notifyStateToListener();
    }

    public void notifyFailureToListener(SHNResult result) {
        if (shnDeviceListener != null) {
            shnDeviceListener.onFailedToConnect(parent, result);
        }
    }

    public void notifyStateToListener() {
        if (shnDeviceListener != null) {
            shnDeviceListener.onStateUpdated(parent);
        }
    }

    public State getState() {
        return state;
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

    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return registeredCapabilities.keySet();
    }

    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return registeredByClassCapabilities.keySet();
    }

    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return registeredCapabilities.get(type);
    }

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

    public <T extends SHNCapability> void registerCapability(@NonNull final Class<? extends SHNCapability> type, @NonNull final T capability) {
        if (registeredByClassCapabilities.containsKey(type)) {
            throw new IllegalStateException("Capability already registered");
        }

        if (capability instanceof SHNCapabilityThreadSafe) {
            ((SHNCapabilityThreadSafe) capability).setHandlers(shnCentral.getInternalHandler(), shnCentral.getUserHandler());
        }

        registeredByClassCapabilities.put(type, capability);
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

    public void setLastDisconnectedTimeMillis(long lastDisconnectedTimeMillis) {
        this.lastDisconnectedTimeMillis = lastDisconnectedTimeMillis;
    }

    public long getLastDisconnectedTimeMillis() {
        return lastDisconnectedTimeMillis;
    }
}
