package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class WaitingUntilBondedState extends State implements SHNCentral.SHNBondStatusListener {
    private static final String TAG = WaitingUntilBondedState.class.getSimpleName();

    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3_000L;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1_000L; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state

    private Timer waitingUntilBondingStartedTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.w(TAG, "Timed out waiting until bonded; trying service discovery");
            goToState(new DiscoveringServicesState(context));
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

    public WaitingUntilBondedState(StateContext context) {
        super(context);

        context.getShnCentral().registerBondStatusListenerForAddress(this, context.getBtDevice().getAddress());

        waitingUntilBondingStartedTimer.restart();

        // Start create bond
        if (context.getShnBondInitiator() == SHNDeviceImpl.SHNBondInitiator.APP) {
            if (!context.getBtDevice().createBond()) {
                SHNLogger.w(TAG, "Failed to start bond creation procedure");
                waitingUntilBondingStartedTimer.stop();
                goToState(new DiscoveringServicesState(context));
            }
        }
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (context.getBtDevice().getAddress().equals(device.getAddress())) {
            SHNLogger.i(TAG, "Bond state changed ('" + bondStateToString(previousBondState) + "' -> '" + bondStateToString(bondState) + "')");

            if (bondState == BluetoothDevice.BOND_BONDING) {
                waitingUntilBondingStartedTimer.restart();
            } else if (bondState == BluetoothDevice.BOND_BONDED) {
                waitingUntilBondingStartedTimer.stop();

                context.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToState(new DiscoveringServicesState(context));
                    }
                }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
            }
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            waitingUntilBondingStartedTimer.stop();
            goToState(new DisconnectingState(context));
        }
    }

    @Override
    public void disconnect() {
        waitingUntilBondingStartedTimer.stop();
        goToState(new DisconnectingState(context));
    }

    private void goToState(State state) {
        context.getShnCentral().unregisterBondStatusListenerForAddress(this, context.getBtDevice().getAddress());
        context.setState(state);
    }

    private static String bondStateToString(int bondState) {
        return (bondState == BluetoothDevice.BOND_NONE) ? "None" :
                (bondState == BluetoothDevice.BOND_BONDING) ? "Bonding" :
                        (bondState == BluetoothDevice.BOND_BONDED) ? "Bonded" : "Unknown";
    }
}
