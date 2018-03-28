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
            context.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            context.setState(new DisconnectingState(context));
        }
    }, CONNECT_TIMEOUT);

    public DiscoveringServicesState(StateContext context) {
        super(context);

        BTGatt btGatt = context.getBtGatt();
        if(btGatt != null) {
            btGatt.discoverServices();
        }
        connectTimer.restart();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onServicesDiscovered(BTGatt gatt, int status) {
        connectTimer.stop();

        if (status == BluetoothGatt.GATT_SUCCESS) {
            context.setState(new InitializingServicesState(context));
        } else {
            SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
            context.setState(new DisconnectingState(context));
        }
    }

    @Override
    public void disconnect() {
        connectTimer.stop();
        context.setState(new DisconnectingState(context));
    }
}
