/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

public class SHNDiscoveringServicesState extends SHNConnectingState {

    private static final long SERVICE_DISCOVERY_TIMEOUT = 20_000L;

    SHNDiscoveringServicesState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, "SHNDiscoveringServicesState", SERVICE_DISCOVERY_TIMEOUT);
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.discoverServices();
        }
    }

    @Override
    public void onServicesDiscovered(BTGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (gatt.getServices().size() == 0) {
                final String errorMsg = "No services found.";

                SHNLogger.w(logTag, errorMsg);
                SHNTagger.sendTechnicalError(errorMsg);

                gatt.disconnect();

                stateMachine.setState(new SHNGattConnectingState(stateMachine));
                return;
            }

            stateMachine.setState(new SHNInitializingServicesState(stateMachine));
        } else {
            final String errorMsg = String.format(Locale.US, "onServicedDiscovered: error discovering services, status [%d]; disconnecting.", status);

            SHNLogger.e(logTag, errorMsg);
            SHNTagger.sendTechnicalError(errorMsg);

            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }
}
