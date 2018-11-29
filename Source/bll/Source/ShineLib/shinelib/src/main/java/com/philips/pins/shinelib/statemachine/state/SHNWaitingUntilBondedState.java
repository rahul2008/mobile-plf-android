/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

public class SHNWaitingUntilBondedState extends SHNConnectingState implements SHNCentral.SHNBondStatusListener {

    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3_000L;
    private static final long BT_STACK_HOLD_OFF_TIME_AFTER_BONDED_IN_MS = 1_000L; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state

    private Timer bondingTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            final String errorMsg = "Timed out waiting until bonded; trying service discovery";

            SHNLogger.w(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.setState(new SHNDiscoveringServicesState(stateMachine));
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

    public SHNWaitingUntilBondedState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, "SHNWaitingUntilBondedState", -1L);
    }

    @Override
    protected void onEnter() {
        sharedResources.getShnCentral().registerBondStatusListenerForAddress(this, sharedResources.getBtDevice().getAddress());

        bondingTimer.restart();

        if (sharedResources.getShnBondInitiator() == SHNDeviceImpl.SHNBondInitiator.APP) {
            if (!sharedResources.getBtDevice().createBond()) {
                final String errorMsg = "Already bonded, bonding or bond creation failed.";

                SHNLogger.w(logTag, errorMsg);
                SHNTagger.sendTechnicalError(errorMsg);

                stateMachine.setState(new SHNDiscoveringServicesState(stateMachine));
            }
        }
    }

    @Override
    protected void onExit() {
        sharedResources.getShnCentral().unregisterBondStatusListenerForAddress(this, sharedResources.getBtDevice().getAddress());
        bondingTimer.stop();
    }

    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (sharedResources.getBtDevice().getAddress().equals(device.getAddress())) {
            SHNLogger.i(logTag, "Bond state changed ('" + bondStateToString(previousBondState) + "' -> '" + bondStateToString(bondState) + "')");

            if (bondState == BluetoothDevice.BOND_BONDING) {
                bondingTimer.restart();
            } else if (bondState == BluetoothDevice.BOND_BONDED) {
                bondingTimer.stop();

                sharedResources.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stateMachine.setState(new SHNDiscoveringServicesState(stateMachine));
                    }
                }, BT_STACK_HOLD_OFF_TIME_AFTER_BONDED_IN_MS);
            } else if (bondState == BluetoothDevice.BOND_NONE) {
                final String errorMsg = String.format(Locale.US, "Bond lost; currentBondState [%d], previousBondState [%d]", bondState, previousBondState);

                SHNLogger.w(logTag, errorMsg);
                SHNTagger.sendTechnicalError(errorMsg);

                stateMachine.notifyFailureToListener(SHNResult.SHNErrorBondLost);
                stateMachine.setState(new SHNDisconnectingState(stateMachine));
            }
        }
    }

    private static String bondStateToString(int bondState) {
        return (bondState == BluetoothDevice.BOND_NONE) ? "None" : (bondState == BluetoothDevice.BOND_BONDING) ? "Bonding" : (bondState == BluetoothDevice.BOND_BONDED) ? "Bonded" : "Unknown";
    }
}
