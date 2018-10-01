/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;

public abstract class SHNConnectingState extends SHNDeviceState {

    private Timer connectingTimer;

    public SHNConnectingState(@NonNull final SHNDeviceStateMachine stateMachine, String loggingTag, long connectTimeOut) {
        super(stateMachine, loggingTag);

        if (connectTimeOut > 0) {
            connectingTimer = Timer.createTimer(new Runnable() {
                @Override
                public void run() {
                    final String errorMsg = "connect timeout in SHNConnectingState";

                    SHNLogger.e(logTag, errorMsg);
                    SHNTagger.sendTechnicalError(errorMsg);

                    stateMachine.getSharedResources().notifyFailureToListener(SHNResult.SHNErrorTimeout);
                    stateMachine.setState(new SHNDisconnectingState(stateMachine));
                }
            }, connectTimeOut);
        }
    }

    @Override
    protected void onEnter() {
        if (connectingTimer != null)
            connectingTimer.restart();
    }

    @Override
    protected void onExit() {
        if (connectingTimer != null)
            connectingTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void disconnect() {
        SHNLogger.d(logTag, "Disconnect call in state SHNConnectingState");
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            final String errorMsg = String.format(Locale.US, "Connection state changed to disconnected, status [%d], newState [%d]", status, newState);

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            handleDisconnectEvent();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (SHNCentralStateNotReady.equals(shnCentral.getShnCentralState())) {
            final String errorMsg = "Not ready for connection to the peripheral.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            handleDisconnectEvent();
        }
    }

    private void handleDisconnectEvent() {
        BTGatt btGatt = stateMachine.getSharedResources().getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        stateMachine.getSharedResources().setBtGatt(null);
        stateMachine.getSharedResources().notifyFailureToListener(SHNResult.SHNErrorInvalidState);
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }
}
