/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.security.InvalidParameterException;
import java.util.Locale;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateReady;

public class SHNGattConnectingState extends SHNConnectingState {

    private static final String TAG = "SHNGattConnectingState";

    private boolean shouldRetryConnecting = false;
    private long minimumConnectionIdleTime;

    private final BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {

        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            SHNLogger.i(TAG, "BTGattCallback - onConnectionStateChange (newState = '" + bluetoothStateToString(newState) + "', status = " + status + ")");
            stateMachine.getState().onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            stateMachine.getState().onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = sharedResources.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = sharedResources.getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            SHNDevice.SHNDeviceListener deviceListener = stateMachine.getDeviceListener();
            if (deviceListener != null) {
                deviceListener.onReadRSSI(rssi);
            }
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {

        }
    };

    SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, "SHNGattConnectingState", -1L);
    }

    SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine, long connectTimeOut) {
        super(stateMachine, "SHNGattConnectingState", connectTimeOut);
        shouldRetryConnecting = true;
        if (connectTimeOut <= 0) {
            throw new InvalidParameterException("Time out can not be negative");
        }
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        setMinimumConnectionIdleTime();
        startConnect();
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            handleGattConnectEvent(status);
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral.State state) {
        if (state == SHNCentralStateNotReady) {
            shouldRetryConnecting = false;
            handleGattDisconnectEvent();
        }
    }

    private void startConnect() {
        final long timeDiff = System.currentTimeMillis() - sharedResources.getLastDisconnectedTimeMillis();
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(timeDiff);
            return;
        }

        if (SHNCentralStateReady.equals(sharedResources.getShnCentral().getShnCentralState())) {
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), btGattCallback, sharedResources.getConnectionPriority()));
        } else {
            final String errorMsg = "Not ready for connection to the peripheral, Bluetooth is not on.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.notifyFailureToListener(SHNResult.SHNErrorBluetoothDisabled);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private void handleGattConnectEvent(int status) {
        SHNLogger.d(logTag, "Handle connect event in SHNGattConnectingState");

        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (shouldWaitUntilBonded()) {
                stateMachine.setState(new SHNWaitingUntilBondedState(stateMachine));
            } else {
                stateMachine.setState(new SHNDiscoveringServicesState(stateMachine));
            }
        } else {
            final String errorMsg = String.format(Locale.US, "Bluetooth GATT connect failure, status [%d]", status);

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        if (shouldRetryConnecting) {
            SHNLogger.d(logTag, "Retrying to connect GATT in SHNGattConnectingState");
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), btGattCallback, sharedResources.getConnectionPriority()));
        } else {
            final String errorMsg = "Bluetooth GATT disconnected, not retrying to connect.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.notifyFailureToListener(SHNResult.SHNErrorInvalidState);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private boolean shouldWaitUntilBonded() {
        return sharedResources.getShnBondInitiator() != SHNDeviceImpl.SHNBondInitiator.NONE && !isBonded();
    }

    private boolean isBonded() {
        return sharedResources.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    private void setMinimumConnectionIdleTime() {
        this.minimumConnectionIdleTime = 2000L;
    }

    private void postponeConnectCall(long timeDiff) {
        SHNLogger.w(logTag, "Postponing connect with " + (minimumConnectionIdleTime - timeDiff) + "ms to allow the stack to properly disconnect");

        sharedResources.getShnCentral().getInternalHandler().postDelayed(this::startConnect, minimumConnectionIdleTime - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return sharedResources.getLastDisconnectedTimeMillis() != 0L && timeDiff < minimumConnectionIdleTime;
    }

    private static String bluetoothStateToString(int bluetoothState) {
        return (bluetoothState == BluetoothProfile.STATE_CONNECTED) ? "Connected" :
                (bluetoothState == BluetoothProfile.STATE_CONNECTING) ? "Connecting" :
                        (bluetoothState == BluetoothProfile.STATE_DISCONNECTED) ? "Disconnected" :
                                (bluetoothState == BluetoothProfile.STATE_DISCONNECTING) ? "Disconnecting" : "Unknown";
    }
}
