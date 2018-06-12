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
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.SHNTagger;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;

public class SHNDisconnectingState extends SHNDeviceState {

    private static final String TAG = "SHNDisconnectingState";

    private static final long DISCONNECT_TIMEOUT = 1_000L;

    private Timer disconnectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "disconnect timeout in SHNDisconnectingState");
            handleGattDisconnectEvent();
        }
    }, DISCONNECT_TIMEOUT);

    public SHNDisconnectingState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void onEnter() {
        disconnectTimer.restart();

        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.disconnect();
        } else {
            handleGattDisconnectEvent();
        }
    }

    @Override
    protected void onExit() {
        disconnectTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Disconnecting;
    }

    @Override
    public void connect() {
        sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);
    }

    @Override
    public void connect(long connectTimeOut) {
        sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            BTGatt btGatt = sharedResources.getBtGatt();
            if (btGatt != null) {
                btGatt.disconnect();
            }
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (SHNCentralStateNotReady.equals(shnCentral.getShnCentralState())) {
            final String errorMsg = "Not ready for connection to the peripheral.";

            SHNLogger.e(TAG, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        for (SHNService shnService : sharedResources.getSHNServices().values()) {
            shnService.disconnectFromBLELayer();
        }

        sharedResources.setLastDisconnectedTimeMillis(System.currentTimeMillis());
        stateMachine.setState(new SHNDisconnectedState(stateMachine));
    }
}
