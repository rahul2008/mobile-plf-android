/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;

public class SHNReadyState extends SHNDeviceState {

    public SHNReadyState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, "SHNReadyState");
    }

    @Override
    protected void onEnter() {

    }

    @Override
    protected void onExit() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connected;
    }

    @Override
    public void connect() {
        sharedResources.notifyStateToListener();
    }

    @Override
    public void connect(long connectTimeOut) {
        sharedResources.notifyStateToListener();
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(logTag, "onServiceStateChanged: " + state + " [" + shnService.getUuid() + "]");

        if (state == SHNService.State.Error) {
            final String errorMsg = String.format(Locale.US, "Service [%s] state changed to error in SHNReadyState, moving to SHNDisconnectingState", shnService.getUuid());

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral.State state) {
        if (state == SHNCentralStateNotReady) {
            final String errorMsg = "Not ready for connection to the peripheral.";

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            handleGattDisconnectEvent();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.d(logTag, "Disconnect call in state SHNReadyState");
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }
}
