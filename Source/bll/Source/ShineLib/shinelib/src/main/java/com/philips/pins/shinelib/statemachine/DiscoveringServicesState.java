package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

public class DiscoveringServicesState extends ConnectingState {

    private static final String TAG = DiscoveringServicesState.class.getSimpleName();


    public DiscoveringServicesState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
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
                SHNLogger.i(TAG, "No services found, rediscovery the services");
                gatt.disconnect();
                stateMachine.setState(this, new GattConnectingState(stateMachine, sharedResources));
                return;
            }

            stateMachine.setState(this, new InitializingServicesState(stateMachine, sharedResources));
        } else {
            SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
        }
    }
}
