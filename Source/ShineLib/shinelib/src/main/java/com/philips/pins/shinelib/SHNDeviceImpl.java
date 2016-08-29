/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
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

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice, SHNCentral.SHNBondStatusListener, SHNCentral.SHNCentralListener {

    public static final long MINIMUM_CONNECTION_IDLE_TIME = 1000L;
    public static final int GATT_ERROR = 0x0085;

    private enum InternalState {
        Disconnected, Disconnecting, GattConnecting, WaitingUntilBonded, DiscoveringServices, InitializingServices, Ready
    }

    private static final String TAG_BASE = SHNDeviceImpl.class.getSimpleName();
    private final String TAG = TAG_BASE + "@" + Integer.toHexString(hashCode());

    private static final long CONNECT_TIMEOUT = 20000L;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1000; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state
    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3000;

    private final boolean deviceBondsDuringConnect;
    private final BTDevice btDevice;
    private final SHNCentral shnCentral;
    private BTGatt btGatt;
    private SHNDeviceListener shnDeviceListener;
    private InternalState internalState = InternalState.Disconnected;
    private String deviceTypeName;
    private long timeOut;
    private long startTimerTime;
    private long lastDisconnectedTimeMillis;

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
            if (BuildConfig.DEBUG && internalState != InternalState.WaitingUntilBonded) {
                throw new IllegalStateException("internalState should be InternalState.WaitingUntilBonded");
            }
            setInternalStateReportStateUpdateAndSetTimers(InternalState.DiscoveringServices);
            btGatt.discoverServices();
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

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
            case GattConnecting:
                connectTimer.stop();
                waitingUntilBondingStartedTimer.stop();
                break;
            case DiscoveringServices:
            case InitializingServices:
                connectTimer.restart();
                waitingUntilBondingStartedTimer.stop();
                break;
            case WaitingUntilBonded:
                connectTimer.stop();
                waitingUntilBondingStartedTimer.restart();
                break;
            case Disconnecting:
            case Disconnected:
            case Ready:
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
        SHNLogger.d(TAG, "Handle connect event in state " + internalState);
        if (internalState == InternalState.Disconnecting) {
            btGatt.disconnect();
        } else {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (shouldWaitUntilBonded()) {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.WaitingUntilBonded);
                } else {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.DiscoveringServices);
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

        long delta = System.currentTimeMillis() - startTimerTime;
        SHNLogger.d(TAG, "delta: " + delta);

        if (internalState == InternalState.GattConnecting && delta < timeOut) {
            SHNLogger.d(TAG, "Retrying to connect GATT in state " + internalState);
            btGatt = btDevice.connectGatt(shnCentral.getApplicationContext(), false, btGattCallback);
        } else {
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
            case GattConnecting:
            case WaitingUntilBonded:
            case DiscoveringServices:
            case InitializingServices:
                return State.Connecting;
            case Ready:
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
        SHNLogger.d(TAG, "Connect call in state " + internalState);

        connect(true, -1L);
    }

    @Override
    public void connect(long connectTimeOut) {
        SHNLogger.d(TAG, "Connect call in state " + internalState + " with timeOut: " + connectTimeOut);

        if (connectTimeOut < 0) {
            throw new InvalidParameterException("Time out can not be negative");
        } else {
            this.startTimerTime = System.currentTimeMillis();
            this.timeOut = connectTimeOut;
            connect(true, -1L);
        }
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        SHNLogger.d(TAG, "Connect call in state " + internalState + " withTimeout: " + withTimeout + " timeoutInMS:" + timeoutInMS);

        final long timeDiff = System.currentTimeMillis() - lastDisconnectedTimeMillis;
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(withTimeout, timeoutInMS, timeDiff);
            return;
        }

        if (shnCentral.isBluetoothAdapterEnabled()) {
            if (internalState == InternalState.Disconnected) {
                SHNLogger.i(TAG, "connect");
                setInternalStateReportStateUpdateAndSetTimers(InternalState.GattConnecting);
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

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return lastDisconnectedTimeMillis != 0L && timeDiff < MINIMUM_CONNECTION_IDLE_TIME;
    }

    private void postponeConnectCall(final boolean withTimeout, final long timeoutInMS, long timeDiff) {
        SHNLogger.w(TAG, "Postponing connect with " + (MINIMUM_CONNECTION_IDLE_TIME - timeDiff) + "ms to allow the stack to properly disconnect");
        shnCentral.getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connect(withTimeout, timeoutInMS);
            }
        }, MINIMUM_CONNECTION_IDLE_TIME - timeDiff);
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state " + internalState);

        switch (internalState) {
            case GattConnecting:
                SHNLogger.i(TAG, "postpone disconnect until connected");
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Disconnecting);
                break;
            case WaitingUntilBonded:
            case DiscoveringServices:
            case InitializingServices:
            case Ready:
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
        if (internalState == InternalState.InitializingServices) {
            if (areAllRegisteredServicesReady()) {
                setInternalStateReportStateUpdateAndSetTimers(InternalState.Ready);
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
            if (internalState == InternalState.DiscoveringServices) {
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    setInternalStateReportStateUpdateAndSetTimers(InternalState.InitializingServices);

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

            if (internalState == InternalState.WaitingUntilBonded) {
                if (bondState == BluetoothDevice.BOND_BONDING) {
                    waitingUntilBondingStartedTimer.stop();
                } else if (bondState == BluetoothDevice.BOND_BONDED) {
                    setInternalStateReportStateUpdateAndSetTimers(InternalState.DiscoveringServices);

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
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
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
