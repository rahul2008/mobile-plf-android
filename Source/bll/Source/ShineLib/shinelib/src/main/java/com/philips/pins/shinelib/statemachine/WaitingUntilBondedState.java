package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class WaitingUntilBondedState extends SHNDeviceState implements SHNCentral.SHNBondStatusListener {

    private static final String TAG = WaitingUntilBondedState.class.getSimpleName();

    private static final long WAIT_UNTIL_BONDED_TIMEOUT_IN_MS = 3_000L;
    private static final long BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS = 1_000L; // Prevent either the Thermometer or the BT stack on some devices from getting in a error state

    private Timer waitingUntilBondingStartedTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.w(TAG, "Timed out waiting until bonded; trying service discovery");
            stateMachine.setState(WaitingUntilBondedState.this, new DiscoveringServicesState(stateMachine, sharedResources));
        }
    }, WAIT_UNTIL_BONDED_TIMEOUT_IN_MS);

    public WaitingUntilBondedState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        sharedResources.getShnCentral().registerBondStatusListenerForAddress(this, sharedResources.getBtDevice().getAddress());

        waitingUntilBondingStartedTimer.restart();

        if (sharedResources.getShnBondInitiator() == SHNDeviceImpl.SHNBondInitiator.APP) {
            if (!sharedResources.getBtDevice().createBond()) {
                SHNLogger.w(TAG, "Failed to start bond creation procedure");
                stateMachine.setState(this, new DiscoveringServicesState(stateMachine, sharedResources));
            }
        }
    }

    @Override
    protected void onExit() {
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
                        stateMachine.setState(WaitingUntilBondedState.this, new DiscoveringServicesState(stateMachine, sharedResources));
                    }
                }, BT_STACK_HOLDOFF_TIME_AFTER_BONDED_IN_MS);
            }
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
        }
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            disconnect();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state WaitingUntilBondedState");
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }

    private static String bondStateToString(int bondState) {
        return (bondState == BluetoothDevice.BOND_NONE) ? "None" :
                (bondState == BluetoothDevice.BOND_BONDING) ? "Bonding" :
                        (bondState == BluetoothDevice.BOND_BONDED) ? "Bonded" : "Unknown";
    }
}
