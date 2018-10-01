/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.workarounds.Workaround;

import java.security.InvalidParameterException;
import java.util.Locale;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateReady;

public class SHNGattConnectingState extends SHNConnectingState {

    private boolean shouldRetryConnecting = false;
    private long minimumConnectionIdleTime;

    public SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, "SHNGattConnectingState", -1L);
    }

    public SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine, long connectTimeOut) {
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
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (SHNCentralStateNotReady.equals(shnCentral.getShnCentralState())) {
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
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback(), sharedResources.getConnectionPriority()));
        } else {
            final String errorMsg = "Not ready for connection to the peripheral, Bluetooth is not on.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            sharedResources.notifyFailureToListener(SHNResult.SHNErrorBluetoothDisabled);
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

            sharedResources.notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
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
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback(), sharedResources.getConnectionPriority()));
        } else {
            final String errorMsg = "Bluetooth GATT disconnected, not retrying to connect.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);
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
        if (Workaround.EXTENDED_MINIMUM_CONNECTION_IDLE_TIME.isRequiredOnThisDevice()) {
            this.minimumConnectionIdleTime = 2000L;
        } else {
            this.minimumConnectionIdleTime = 1000L;
        }
    }

    private void postponeConnectCall(long timeDiff) {
        SHNLogger.w(logTag, "Postponing connect with " + (minimumConnectionIdleTime - timeDiff) + "ms to allow the stack to properly disconnect");

        sharedResources.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startConnect();
            }
        }, minimumConnectionIdleTime - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return sharedResources.getLastDisconnectedTimeMillis() != 0L && timeDiff < minimumConnectionIdleTime;
    }
}
