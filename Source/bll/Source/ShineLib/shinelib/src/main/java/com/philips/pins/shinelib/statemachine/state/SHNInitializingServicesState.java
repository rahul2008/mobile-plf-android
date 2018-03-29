package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;

public class SHNInitializingServicesState extends SHNConnectingState {

    private static final String TAG = SHNInitializingServicesState.class.getSimpleName();

    public SHNInitializingServicesState(SHNDeviceStateMachine stateMachine) {
        super(stateMachine);
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
