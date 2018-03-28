package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;

public class SHNDiscoveringServicesState extends SHNConnectingState {

    private static final String TAG = SHNDiscoveringServicesState.class.getSimpleName();


    public SHNDiscoveringServicesState(SHNDeviceStateMachine stateMachine) {
        super(stateMachine);
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
                stateMachine.setState(this, new SHNGattConnectingState(stateMachine));
                return;
            }

            stateMachine.setState(this, new SHNInitializingServicesState(stateMachine));
        } else {
            SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
            stateMachine.setState(this, new SHNDisconnectingState(stateMachine));
        }
    }
}
