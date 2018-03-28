package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class WaitingUntilBondedState extends State {
    private static final String TAG = WaitingUntilBondedState.class.getSimpleName();

    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3000;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1000; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state

    private Timer waitingUntilBondingStartedTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.w(TAG, "Timed out waiting until bonded; trying service discovery");
            waitingUntilBondingStartedTimer.stop();
            context.setState(new DiscoveringServicesState(context));
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

    public WaitingUntilBondedState(StateContext context) {
        super(context);

        waitingUntilBondingStartedTimer.restart();

        // Start create bond
        if (context.getShnBondInitiator() == SHNDeviceImpl.SHNBondInitiator.APP) {
            if (!context.getBtDevice().createBond()) {
                SHNLogger.w(TAG, "Failed to start bond creation procedure");
                waitingUntilBondingStartedTimer.stop();
                context.setState(new DiscoveringServicesState(context));
            }
        }
    }

    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (context.getBtDevice().getAddress().equals(device.getAddress())) {
            //SHNLogger.i(TAG, "Bond state changed ('" + bondStateToString(previousBondState) + "' -> '" + bondStateToString(bondState) + "')");

            if (bondState == BluetoothDevice.BOND_BONDING) {
                waitingUntilBondingStartedTimer.stop();
            } else if (bondState == BluetoothDevice.BOND_BONDED) {

                waitingUntilBondingStartedTimer.stop();

                context.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.setState(new DiscoveringServicesState(context));
                    }
                }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
            }
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            context.setState(new DisconnectingState(context));
        }
    }
}
