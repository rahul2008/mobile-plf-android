package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;

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
            stateMachine.setState(WaitingUntilBondedState.this, new DiscoveringServicesState(stateMachine));
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

    public WaitingUntilBondedState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void setup() {
        sharedResources.getShnCentral().registerBondStatusListenerForAddress(this, sharedResources.getBtDevice().getAddress());

        waitingUntilBondingStartedTimer.restart();

        // Start create bond
        if (sharedResources.getShnBondInitiator() == SHNDeviceImpl.SHNBondInitiator.APP) {
            if (!sharedResources.getBtDevice().createBond()) {
                SHNLogger.w(TAG, "Failed to start bond creation procedure");
                stateMachine.setState(this, new DiscoveringServicesState(stateMachine));
            }
        }
    }

    @Override
    public void breakdown() {
        sharedResources.getShnCentral().unregisterBondStatusListenerForAddress(this, sharedResources.getBtDevice().getAddress());
        waitingUntilBondingStartedTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        if (sharedResources.getBtDevice().getAddress().equals(device.getAddress())) {
            SHNLogger.i(TAG, "Bond state changed ('" + bondStateToString(previousBondState) + "' -> '" + bondStateToString(bondState) + "')");

            if (bondState == BluetoothDevice.BOND_BONDING) {
                waitingUntilBondingStartedTimer.restart();
            } else if (bondState == BluetoothDevice.BOND_BONDED) {
                waitingUntilBondingStartedTimer.stop();

                sharedResources.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stateMachine.setState(WaitingUntilBondedState.this, new DiscoveringServicesState(stateMachine));
                    }
                }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
            }
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            stateMachine.setState(this, new DisconnectingState(stateMachine));
        }
    }

    @Override
    public void disconnect() {
        stateMachine.setState(this, new DisconnectingState(stateMachine));
    }
    private static String bondStateToString(int bondState) {
        return (bondState == BluetoothDevice.BOND_NONE) ? "None" :
                (bondState == BluetoothDevice.BOND_BONDING) ? "Bonding" :
                        (bondState == BluetoothDevice.BOND_BONDED) ? "Bonded" : "Unknown";
    }
}
