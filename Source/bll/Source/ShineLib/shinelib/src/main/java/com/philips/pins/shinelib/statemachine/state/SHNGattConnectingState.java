package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.workarounds.Workaround;

import java.security.InvalidParameterException;

public class SHNGattConnectingState extends SHNConnectingState {

    private static final String TAG = SHNGattConnectingState.class.getName();

    private boolean shouldRetryConnecting = false;
    private long minimumConnectionIdleTime;

    public SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine, -1L);
    }

    public SHNGattConnectingState(@NonNull SHNDeviceStateMachine stateMachine, long connectTimeOut) {
        super(stateMachine, connectTimeOut);
        shouldRetryConnecting = true;
        if (connectTimeOut <= 0) {
            throw new InvalidParameterException("Time out can not be negative");
        }
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        setMinimumConnectionIdleTime();
        startConnect();
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            handleGattConnectEvent(status);
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            shouldRetryConnecting = false; // make sure the retry is not issued
            handleGattDisconnectEvent();
        }
    }

    private void startConnect() {
        final long timeDiff = System.currentTimeMillis() - sharedResources.getLastDisconnectedTimeMillis();
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(timeDiff);
            return;
        }

        if (sharedResources.getShnCentral().isBluetoothAdapterEnabled()) {
            sharedResources.getShnCentral().registerSHNCentralStatusListenerForAddress(sharedResources.getShnCentralListener(), sharedResources.getBtDevice().getAddress());
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback()));
        } else {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorBluetoothDisabled);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private void handleGattConnectEvent(int status) {
        SHNLogger.d(TAG, "Handle connect event in SHNGattConnectingState");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (shouldWaitUntilBonded()) {
                stateMachine.setState(new SHNWaitingUntilBondedState(stateMachine));
            } else {
                stateMachine.setState(new SHNDiscoveringServicesState(stateMachine));
            }
        } else {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        if (shouldRetryConnecting) {
            SHNLogger.d(TAG, "Retrying to connect GATT in SHNGattConnectingState");
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback()));
        } else {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
        }
    }

    private boolean shouldWaitUntilBonded() {
        return sharedResources.getShnBondInitiator() != SHNDeviceImpl.SHNBondInitiator.NONE && !isBonded();
    }

    private boolean isBonded() {
        return sharedResources.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    private void setMinimumConnectionIdleTime() {
        if (Workaround.EXTENDED_MINIMUM_CONNECTION_IDLE_TIME.isRequiredOnThisDevice()) {
            this.minimumConnectionIdleTime = 2000L;
        } else {
            this.minimumConnectionIdleTime = 1000L;
        }
    }

    private void postponeConnectCall(long timeDiff) {
        SHNLogger.w(TAG, "Postponing connect with " + (minimumConnectionIdleTime - timeDiff) + "ms to allow the stack to properly disconnect");

        sharedResources.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startConnect();
            }
        }, minimumConnectionIdleTime - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return sharedResources.getLastDisconnectedTimeMillis() != 0L && timeDiff < minimumConnectionIdleTime;
    }
}
