package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class DiscoveringServicesState extends State {

    private static final String TAG = DiscoveringServicesState.class.getSimpleName();

    private static final long CONNECT_TIMEOUT = 20000L;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            stateMachine.setState(DiscoveringServicesState.this, new DisconnectingState(stateMachine));
        }
    }, CONNECT_TIMEOUT);

    public DiscoveringServicesState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void setup() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.discoverServices();
        }
        connectTimer.restart();
    }

    @Override
    public void breakdown() {
        connectTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onServicesDiscovered(BTGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            stateMachine.setState(this, new InitializingServicesState(stateMachine));
        } else {
            SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
            stateMachine.setState(this, new DisconnectingState(stateMachine));
        }
    }

    @Override
    public void disconnect() {
        stateMachine.setState(this, new DisconnectingState(stateMachine));
    }
}
