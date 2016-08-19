/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

/*
@startuml
[*] --> Disconnected
Disconnected --> Connecting : connect /\nconnectGatt, restartConnectTimer, onStateChange(Connecting)
Connecting --> Disconnected : onConnectionStateChange(Disconnected) /\nclose, stopConnectTimer, onFailedToConnect, onStateChange(Disconnected)
Connecting --> Disconnecting : connectTimeout /\ndisconnect, onFailedToConnect, onStateChange(Disconnecting)
Connecting --> ConnectedWaitingUntilBonded : onConnectionStateChange(Connected) & waitForBond /\nstopConnectTimer, startBondCreationTimer
ConnectedWaitingUntilBonded --> ConnectedDiscoveringServices : bondCreated | bondCreationTimeout /\ndiscoverServices, stopBondCreationTimer, restartConnectionTimer
Connecting --> ConnectedDiscoveringServices : onConnectionStateChange(Connected) & !waitForBond /\ndiscoverServices, restartConnectionTimer
ConnectedDiscoveringServices --> ConnectedInitializingServices : onServicesDiscovered /\nconnectToBle, restartConnectionTimer
ConnectedInitializingServices --> ConnectedReady : all services ready /\nstopConnectionTimer, onStateChange(Connected)
ConnectedDiscoveringServices --> Disconnecting : connectTimeout /\ndisconnect, onFailedToConnect, onStateChange(Disconnecting)
ConnectedInitializingServices --> Disconnecting : connectTimeout /\ndisconnect, disconnectFromBle, onFailedToConnect, onStateChange(Disconnecting)
ConnectedReady --> Disconnecting : disconnect /\ndisconnect, disconnectFromBle, onStateChange(Disconnecting)
Disconnecting --> Disconnected : onConnectionStateChange(Disconnected) /\nclose, onStateChange(Disconnected)
ConnectedReady --> Disconnected : onConnectionStateChange(Disconnected) /\nclose, onStateChange(Disconnected)
@enduml
 */
package com.philips.pins.shinelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNBondStatusListener, SHNCentral.SHNCentralListener {

    public static final long MINIMUM_CONNECTION_IDLE_TIME = 1000L;

    private enum InternalState {
        Disconnected, Disconnecting, Connecting, ConnectedWaitingUntilBonded, ConnectedDiscoveringServices, ConnectedInitializingServices, ConnectedReady
    }

    private static final String TAG_BASE = SHNDeviceImpl.class.getSimpleName();
    private final String TAG = TAG_BASE + "@" + Integer.toHexString(hashCode());

    private static final long CONNECT_TIMEOUT = 20000L;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1000; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state
    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3000;

    private final Boolean deviceBondsDuringConnect;
    private final BTDevice btDevice;
    private final SHNCentral shnCentral;
    private BTGatt btGatt;
    private SHNDeviceListener shnDeviceListener;
    private InternalState internalState = InternalState.Disconnected;
    private String deviceTypeName;
    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Map<UUID, SHNService> registeredServices = new HashMap<>();
    private SHNResult failedToConnectResult = SHNResult.SHNOk;
    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "connect timeout in state: " + internalState);
            failedToConnectResult = SHNResult.SHNErrorTimeout;
            disconnect();
        }
    }, CONNECT_TIMEOUT);
    private Timer waitingUntilBondingStartedTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.w(TAG, "Timed out waiting until bonded; trying service discovery");
            if (BuildConfig.DEBUG && internalState == InternalState.ConnectedWaitingUntilBonded)
                throw new AssertionError();
            setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedDiscoveringServices);
            btGatt.discoverServices();
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);
    private long lastDisconnectedTimeMillis;

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName) {
        this(btDevice, shnCentral, deviceTypeName, false);
    }

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, boolean deviceBondsDuringConnect) {
        this.deviceBondsDuringConnect = deviceBondsDuringConnect;
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.deviceTypeName = deviceTypeName;

        SHNLogger.i(TAG, "Created new instance of SHNDevice for type: " + deviceTypeName + " address: " + btDevice.getAddress());
    }

    private void setInternalStateReportStateUpdateAndSetTimers(InternalState newInternalState) {
        if (internalState != newInternalState) {
            SHNLogger.i(TAG, "State changed ('" + internalState.toString() + "' -> '" + newInternalState.toString() + "')");
            State oldExternalState = convertInternalStateToExternalState(internalState);
            State newExternalState = convertInternalStateToExternalState(newInternalState);
            internalState = newInternalState;

            reportStateUpdate(oldExternalState, newExternalState);
            setTimers();

            if (internalState == InternalState.Disconnected) {
                shnCentral.unregisterSHNCentralStatusListenerForAddress(this, getAddress());
                lastDisconnectedTimeMillis = System.currentTimeMillis();
            }
        }
    }

    private void reportStateUpdate(State oldExternalState, State newExternalState) {
        if (oldExternalState != newExternalState) {
            if (newExternalState == State.Disconnected && failedToConnectResult != SHNResult.SHNOk) {
                if (shnDeviceListener != null) {
                    shnDeviceListener.onFailedToConnect(this, failedToConnectResult);
                }
                failedToConnectResult = SHNResult.SHNOk;
            }
            if (shnDeviceListener != null) {
                shnDeviceListener.onStateUpdated(this);
            }
        }
    }

    private void setTimers() {
        switch (internalState) {
            case Connecting:
                connectTimer.stop();
                waitingUntilBondingStartedTimer.stop();
                break;
            case ConnectedDiscoveringServices:
            case ConnectedInitializingServices:
                connectTimer.restart();
                waitingUntilBondingStartedTimer.stop();
                break;
            case ConnectedWaitingUntilBonded:
                connectTimer.stop();
                waitingUntilBondingStartedTimer.restart();
                break;
            case Disconnecting:
            case Disconnected:
            case ConnectedReady:
                connectTimer.stop();
                waitingUntilBondingStartedTimer.stop();
                break;
        }
    }

    private void connectUsedServicesToBleLayer(BTGatt gatt) {
        for (BluetoothGattService bluetoothGattService : btGatt.getServices()) {
            SHNService shnService = getSHNService(bluetoothGattService.getUuid());
            SHNLogger.i(TAG, "onServicedDiscovered: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not used by plugin" : " connecting plugin service to ble service"));
            if (shnService != null) {
                shnService.connectToBLELayer(gatt, bluetoothGattService);
            }
        }
    }

    private void handleGattConnectEvent(int status) {
        if (internalState == InternalState.Disconnecting) {
            btGatt.disconnect();
        } else {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (shouldWaitUntilBonded()) {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedWaitingUntilBonded);
                } else {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedDiscoveringServices);
                    btGatt.discoverServices();
                }
            } else {
                failedToConnectResult = SHNResult.SHNErrorConnectionLost;
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Disconnecting);
                btGatt.disconnect();
            }
        }
    }

    private void handleGattDisconnectEvent() {
        btGatt.close();
        btGatt = null;
        for (SHNService shnService : registeredServices.values()) {
            shnService.disconnectFromBLELayer();
        }
        if (getState() == State.Connecting) {
            failedToConnectResult = SHNResult.SHNErrorInvalidState;
        }
        setInternalStateReportStateUpdateAndSetTimers(InternalState.Disconnected);
        shnCentral.unregisterBondStatusListenerForAddress(SHNDeviceImpl.this, getAddress());
        shnCentral.unregisterSHNCentralStatusListenerForAddress(SHNDeviceImpl.this, getAddress());
    }

    private boolean shouldWaitUntilBonded() {
        return deviceBondsDuringConnect && btDevice.getBondState() != BluetoothDevice.BOND_BONDED;
    }

    @NonNull
    private State convertInternalStateToExternalState(InternalState state) {
        switch (state) {
            case Disconnected:
                return State.Disconnected;
            case Disconnecting:
                return State.Disconnecting;
            case Connecting:
            case ConnectedWaitingUntilBonded:
            case ConnectedDiscoveringServices:
            case ConnectedInitializingServices:
                return State.Connecting;
            case ConnectedReady:
                return State.Connected;
            default:
                if (BuildConfig.DEBUG) throw new AssertionError();
                break;
        }
        return null;
    }

    public boolean isBonded() {
        return btDevice.getBondState() == BluetoothDevice.BOND_BONDED;
    }

    // implements SHNDevice
    @Override
    public State getState() {
        return convertInternalStateToExternalState(internalState);
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
        connect(true, -1L);
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        final long timeDiff = System.currentTimeMillis() - lastDisconnectedTimeMillis;
        if (lastDisconnectedTimeMillis != 0L && timeDiff < MINIMUM_CONNECTION_IDLE_TIME) {
            SHNLogger.w(TAG, "Postponing connect with " + (MINIMUM_CONNECTION_IDLE_TIME - timeDiff) + "ms to allow the stack to properly disconnect");
            shnCentral.getInternalHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect(withTimeout, timeoutInMS);
                }
            }, MINIMUM_CONNECTION_IDLE_TIME - timeDiff);
            return;
        }
        if (shnCentral.isBluetoothAdapterEnabled()) {
            if (internalState == InternalState.Disconnected) {
                SHNLogger.i(TAG, "connect");
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Connecting);
                shnCentral.registerBondStatusListenerForAddress(this, getAddress());
                shnCentral.registerSHNCentralStatusListenerForAddress(this, getAddress());
                if (withTimeout) {
                    if (timeoutInMS > 0) {
                        connectTimer.setTimeoutForSubsequentRestartsInMS(timeoutInMS);
                    }
                    btGatt = btDevice.connectGatt(shnCentral.getApplicationContext(), false, btGattCallback);
                } else {
                    btGatt = btDevice.connectGatt(shnCentral.getApplicationContext(), true, btGattCallback);
                }
            } else {
                SHNLogger.i(TAG, "ignoring 'connect' call; not disconnected");
            }
        } else {
            if (shnDeviceListener != null) {
                shnDeviceListener.onStateUpdated(this);
            }
        }
    }

    @Override
    public void disconnect() {
        switch (internalState) {
            case Connecting:
                SHNLogger.i(TAG, "postpone disconnect until connected");
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Disconnecting);
                break;
            case ConnectedWaitingUntilBonded:
            case ConnectedDiscoveringServices:
            case ConnectedInitializingServices:
            case ConnectedReady:
                SHNLogger.i(TAG, "disconnect");
                btGatt.disconnect();
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Disconnecting);
                break;
            case Disconnected:
            case Disconnecting:
                SHNLogger.i(TAG, "ignoring 'disconnect' call; already disconnected or disconnecting");
                break;
            default:
                if (BuildConfig.DEBUG) throw new AssertionError();
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

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return registeredCapabilities.keySet();
    }

    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return registeredCapabilities.get(type);
    }

    /**
     * Register a capability for this device.
     *
     * @param shnCapability An actual implementation for a capability.
     * @param type          The type of capability the shnCapability implements.
     */
    public void registerCapability(@NonNull final SHNCapability shnCapability, @NonNull final SHNCapabilityType type) {
        if (registeredCapabilities.containsKey(type)) {
            throw new IllegalStateException("Capability already registered");
        }

        SHNCapability shnCapabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(shnCapability, type, shnCentral.getInternalHandler(), shnCentral.getUserHandler());
        registeredCapabilities.put(type, shnCapabilityWrapper);

        SHNCapabilityType counterPart = SHNCapabilityType.getCounterPart(type);
        if (counterPart != null) {
            registeredCapabilities.put(counterPart, shnCapabilityWrapper);
        }
    }

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
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");
        if (internalState == InternalState.ConnectedInitializingServices) {
            if (areAllRegisteredServicesReady()) {
                setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedReady);
            }
        }
        if (state == SHNService.State.Error) {
            disconnect();
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
            SHNLogger.i(TAG, "BTGattCallback - onConnectionStateChange (newState = '" + bluetoothStateToString(newState) + "', status = " + status + ")");

            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                handleGattDisconnectEvent();
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                handleGattConnectEvent(status);
            }
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            if (internalState == InternalState.ConnectedDiscoveringServices) {
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedInitializingServices);

                    connectUsedServicesToBleLayer(gatt);
                } else {
                    SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
                    disconnect();
                }
            } else {
                SHNLogger.w(TAG, "onServicedDiscovered: unexpected call while in state '" + internalState.toString() + "'; ignoring");
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

    // implements SHNCentral.SHNBondStatusListener
    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (btDevice.getAddress().equals(device.getAddress())) {
            SHNLogger.i(TAG, "Bond state changed ('" + bondStateToString(previousBondState) + "' -> '" + bondStateToString(bondState) + "')");

            if (internalState == InternalState.ConnectedWaitingUntilBonded) {
                if (bondState == BluetoothDevice.BOND_BONDING) {
                    waitingUntilBondingStartedTimer.stop();
                } else if (bondState == BluetoothDevice.BOND_BONDED) {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.ConnectedDiscoveringServices);

                    shnCentral.getInternalHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (btGatt != null) {
                                btGatt.discoverServices();
                            }
                        }
                    }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
                } else if (bondState == BluetoothDevice.BOND_NONE) {
                    disconnect();
                }
            }
        }
    }

    // implements SHNCentral.SHNCentralListener
    @Override
    public void onStateUpdated(SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            SHNLogger.i(TAG, "BluetoothAdapter disabled");
            if (internalState != InternalState.Disconnected) {
                SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
                handleGattDisconnectEvent();
            }
        }
    }

    private static String bluetoothStateToString(int bluetoothState) {
        return (bluetoothState == BluetoothProfile.STATE_CONNECTED) ? "Connected" :
                (bluetoothState == BluetoothProfile.STATE_CONNECTING) ? "Connecting" :
                        (bluetoothState == BluetoothProfile.STATE_DISCONNECTED) ? "Disconnected" :
                                (bluetoothState == BluetoothProfile.STATE_DISCONNECTING) ? "Disconnecting" : "Unknown";
    }

    private static String bondStateToString(int bondState) {
        return (bondState == BluetoothDevice.BOND_NONE) ? "None" :
                (bondState == BluetoothDevice.BOND_BONDING) ? "Bonding" :
                        (bondState == BluetoothDevice.BOND_BONDED) ? "Bonded" : "Unknown";
    }
}
