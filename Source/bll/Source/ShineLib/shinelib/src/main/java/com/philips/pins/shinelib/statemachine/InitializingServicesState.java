package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

public class InitializingServicesState extends ConnectingState {

    private static final String TAG = InitializingServicesState.class.getSimpleName();

    public InitializingServicesState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
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
            stateMachine.setState(this, new ReadyState(stateMachine, sharedResources));
        }

        if (state == SHNService.State.Error) {
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
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
