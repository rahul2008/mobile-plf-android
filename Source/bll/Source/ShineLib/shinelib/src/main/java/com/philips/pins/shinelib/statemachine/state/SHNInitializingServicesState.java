/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;

public class SHNInitializingServicesState extends SHNConnectingState {

    private static final String TAG = "SHNInitializingServicesState";
    private static final long SERVICE_INITIALIZATION_TIMEOUT = 20_000L;

    public SHNInitializingServicesState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, SERVICE_INITIALIZATION_TIMEOUT);
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        connectUsedServicesToBleLayer();
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        if (areAllRegisteredServicesReady()) {
            stateMachine.setState(new SHNReadyState(stateMachine));
        }

        if (state == SHNService.State.Error) {
            stateMachine.setState(new SHNDisconnectingState(stateMachine));

            // TODO send tag with which service in error?
            stateMachine.getSharedResources().getShnCentral().getTagger().sendTechnicalError("Error initializing services");
        }
    }

    private void connectUsedServicesToBleLayer() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt == null) {
            return;
        }

        for (BluetoothGattService bluetoothGattService : btGatt.getServices()) {
            SHNService shnService = sharedResources.getSHNService(bluetoothGattService.getUuid());
            SHNLogger.i(TAG, "onServicedDiscovered: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not used by plugin" : " connecting plugin service to ble service"));

            SHNDevice.DiscoveryListener discoveryListener = sharedResources.getDiscoveryListener();
            if (discoveryListener != null) {
                discoveryListener.onServiceDiscovered(bluetoothGattService.getUuid(), shnService);
            }

            if (shnService != null) {
                shnService.connectToBLELayer(btGatt, bluetoothGattService);
            }
        }
    }

    private boolean areAllRegisteredServicesReady() {
        Boolean allReady = true;
        for (SHNService service : sharedResources.getSHNServices().values()) {
            if (service.getState() != SHNService.State.Ready) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }
}
