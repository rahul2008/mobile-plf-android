/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNBondStatusListener {
    enum InternalState {
        Disconnected, Disconnecting, Connecting, ConnectedWaitingUntilBonded, ConnectedDiscoveringServices, ConnectedInitializingServices, ConnectedReady
    }

    private static final String TAG = SHNDeviceImpl.class.getSimpleName();
    private static final long CONNECT_TIMEOUT = 10000l;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1000;
    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3000;
    private final Boolean deviceBondsDuringConnect;
    private final BTDevice btDevice;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BTGatt btGatt;
    private Timer connectTimer;
    private Timer waitingUntilBondedTimer;
    private SHNDeviceListener shnDeviceListener;
    private InternalState internalState = InternalState.Disconnected;
    private String deviceTypeName;

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName) {
        this(btDevice, shnCentral, deviceTypeName, false);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, boolean deviceBondsDuringConnect) {
        this.deviceBondsDuringConnect = deviceBondsDuringConnect;
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.deviceTypeName = deviceTypeName;
        this.applicationContext = shnCentral.getApplicationContext();
        this.connectTimer = new Timer(shnCentral.getInternalHandler(), new Runnable() {
            @Override
            public void run() {
                SHNLogger.e(TAG, "connect timeout");
                disconnect();
            }
        }, CONNECT_TIMEOUT);
        this.waitingUntilBondedTimer = new Timer(shnCentral.getInternalHandler(), new Runnable() {
            @Override
            public void run() {
                SHNLogger.w(TAG, "Timed out waiting until bonded; trying service discovery");
                assert (internalState == InternalState.ConnectedWaitingUntilBonded);
                setInternalState(InternalState.ConnectedDiscoveringServices);
                btGatt.discoverServices();
            }
        }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);
    }

    private void setInternalState(InternalState newInternalState) {
        if (internalState != newInternalState) {
            SHNLogger.i(TAG, "State changed ('" + internalState.toString() + "' -> '" + newInternalState.toString() + "')");
            State oldState = getState();
            internalState = newInternalState;
            if (getState() != oldState) {
                if (shnDeviceListener != null) {
                    shnDeviceListener.onStateUpdated(this);
                }
            }
        }
    }

    @Override
    public State getState() {
        State state = State.Disconnected;
        switch (internalState) {
            case Disconnected:
                state = State.Disconnected;
                break;
            case Disconnecting:
                state = State.Disconnecting;
                break;
            case Connecting:
            case ConnectedWaitingUntilBonded:
            case ConnectedDiscoveringServices:
            case ConnectedInitializingServices:
                state = State.Connecting;
                break;
            case ConnectedReady:
                state = State.Connected;
                break;
            default:
                assert (false);
                break;
        }
        return state;
    }

    @Override
    public String getAddress() {
        return btDevice.getAddress();
    }

    @Override
    public String getName() {
        return btDevice.getName();
    }

    @Override
    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    @Override
    public void connect() {
        SHNLogger.i(TAG, "connect");
        shnCentral.registerBondStatusListenerForAddress(this, getAddress());
        if (internalState == InternalState.Disconnected) {
            setInternalState(InternalState.Connecting);
            btGatt = btDevice.connectGatt(applicationContext, false, btGattCallback);
            connectTimer.restart();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.e(TAG, "disconnect");
        if (internalState != InternalState.Disconnected && internalState != InternalState.Disconnecting) {
            setInternalState(InternalState.Disconnecting);
            connectTimer.stop();
            waitingUntilBondedTimer.stop();
            if (btGatt != null) {
                btGatt.disconnect();
            }
        }
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        throw new UnsupportedOperationException("Intended for the external API");
    }

    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Set<SHNCapabilityType> registeredCapabilityTypes = new HashSet<>();

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return Collections.unmodifiableSet(registeredCapabilityTypes);
    }

    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return registeredCapabilities.get(type);
    }

    /**
     * Register a capability for this device.
     *
     * @param shnCapability     An actual implementation for a capability.
     * @param shnCapabilityType The type of capability the shnCapability implements.
     */
    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType shnCapabilityType) {
        if(SHNCapabilityType.isDeprecated(shnCapabilityType)){
            registerCapabilityIgnoreDeprecation(shnCapability, SHNCapabilityType.fixDeprecation(shnCapabilityType));
        } else{
            registerCapabilityIgnoreDeprecation(shnCapability, SHNCapabilityType.getDeprecated(shnCapabilityType));
        }

        registerCapabilityIgnoreDeprecation(shnCapability, shnCapabilityType);
    }

    private void registerCapabilityIgnoreDeprecation(final @NonNull SHNCapability shnCapability, final @NonNull SHNCapabilityType shnCapabilityType) {
        if (registeredCapabilities.containsKey(shnCapabilityType)) {
            throw new IllegalStateException("Capability already registered");
        }

        SHNCapability shnCapabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(shnCapability, shnCapabilityType, shnCentral.getInternalHandler(), shnCentral.getUserHandler());

        registeredCapabilityTypes.add(shnCapabilityType);
        registeredCapabilities.put(shnCapabilityType, shnCapabilityWrapper);
    }

    private Map<UUID, SHNService> registeredServices = new HashMap<>();

    public void registerService(SHNService shnService) {
        registeredServices.put(shnService.getUuid(), shnService);
        shnService.registerSHNServiceListener(this);
    }

    private SHNService getSHNService(UUID serviceUUID) {
        return registeredServices.get(serviceUUID);
    }

    // SHNServiceListener callback
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.e(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");
        if (internalState == InternalState.ConnectedInitializingServices) {
            if (areAllRegisteredServicesReady()) {
                connectTimer.stop();
                setInternalState(InternalState.ConnectedReady);
            }
        }
    }

    private Boolean areAllRegisteredServicesReady() {
        Boolean allReady = true;
        for (SHNService service : registeredServices.values()) {
            if (service.getState() != SHNService.State.Ready) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }

    @Override
    public String toString() {
        return "SHNDevice - " + btDevice.getName() + " [" + btDevice.getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {
        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (btGatt != null) {
                    btGatt.close();
                    btGatt = null;
                }
                for (SHNService shnService : registeredServices.values()) {
                    shnService.disconnectFromBLELayer();
                }
                setInternalState(InternalState.Disconnected);
                shnCentral.unregisterBondStatusListenerForAddress(SHNDeviceImpl.this, getAddress());
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (shouldWaitUntilBonded()) {
                    setInternalState(InternalState.ConnectedWaitingUntilBonded);
                    waitingUntilBondedTimer.restart();
                } else {
                    setInternalState(InternalState.ConnectedDiscoveringServices);
                    gatt.discoverServices();
                }
            }
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            assert (internalState == InternalState.ConnectedDiscoveringServices);

            SHNLogger.i(TAG, "handleOnServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (BluetoothGattService bluetoothGattService : btGatt.getServices()) {
                    SHNService shnService = getSHNService(bluetoothGattService.getUuid());
                    SHNLogger.i(TAG, "handleOnServicesDiscovered service: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not found" : " connecting"));
                    if (shnService != null) {
                        shnService.connectToBLELayer(gatt, bluetoothGattService);
                    }
                }

                setInternalState(InternalState.ConnectedInitializingServices);
            } else {
                disconnect();
            }
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            throw new UnsupportedOperationException("onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {
        }
    };

    private boolean shouldWaitUntilBonded() {
        return deviceBondsDuringConnect && btDevice.getBondState() != BluetoothDevice.BOND_BONDED;
    }

    // implements SHNCentral.SHNBondStatusListener
    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (btDevice.getAddress().equals(device.getAddress())) {
            if (bondState == BluetoothDevice.BOND_BONDING) {
                connectTimer.stop();
            } else {
                connectTimer.restart();
            }

            if (internalState == InternalState.ConnectedWaitingUntilBonded) {
                if (bondState == BluetoothDevice.BOND_BONDING) {
                    waitingUntilBondedTimer.stop();
                } else if (bondState == BluetoothDevice.BOND_BONDED) {
                    assert (btGatt != null);
                    setInternalState(InternalState.ConnectedDiscoveringServices);
                    waitingUntilBondedTimer.stop();

                    shnCentral.getInternalHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btGatt.discoverServices();
                        }
                    }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
                } else if (bondState == BluetoothDevice.BOND_NONE) {
                    disconnect();
                }
            }
        }
    }
}
