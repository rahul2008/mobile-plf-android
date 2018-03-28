package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.workarounds.Workaround;

import java.security.InvalidParameterException;

public class GattConnectingState extends State {

    private static final String TAG = GattConnectingState.class.getName();

    private static final long CONNECT_TIMEOUT = 20_000L;

    private final boolean withTimeout;
    private final long timeoutInMS;

    private long timeOut;
    private long startTimerTime;
    private long minimumConnectionIdleTime;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            disconnect();
        }
    }, CONNECT_TIMEOUT);

    public GattConnectingState(StateMachine stateMachine) {
        this(stateMachine, true, -1L);
    }

    public GattConnectingState(StateMachine stateMachine, long connectTimeOut) {
        this(stateMachine, true, -1L);

        if (connectTimeOut < 0) {
            throw new InvalidParameterException("Time out can not be negative");
        } else {
            this.startTimerTime = System.currentTimeMillis();
            this.timeOut = connectTimeOut;
        }
    }

    public GattConnectingState(StateMachine stateMachine, final boolean withTimeout, final long timeoutInMS) {
        super(stateMachine);
        this.withTimeout = withTimeout;
        this.timeoutInMS = timeoutInMS;
    }

    @Override
    public void setup() {
        setMinimumConnectionIdleTime();
        startConnect(withTimeout, timeoutInMS);
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
    public void disconnect() {
        SHNLogger.w(TAG, "to disconnect state");
        stateMachine.setState(this, new DisconnectingState(stateMachine));
    }

    private void startConnect(final boolean withTimeout, final long timeoutInMS) {
        final long timeDiff = System.currentTimeMillis() - sharedResources.getLastDisconnectedTimeMillis();
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(withTimeout, timeoutInMS, timeDiff);
            return;
        }

        if (sharedResources.getShnCentral().isBluetoothAdapterEnabled()) {
            sharedResources.getShnCentral().registerSHNCentralStatusListenerForAddress(sharedResources.getShnCentralListener(), sharedResources.getBtDevice().getAddress());
            if (withTimeout) {
                if (timeoutInMS > 0) {
                    connectTimer.setTimeoutForSubsequentRestartsInMS(timeoutInMS);
                }
                sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback()));
            } else {
                sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), true, sharedResources.getShnCentral(), sharedResources.getBTGattCallback()));
            }
        } else {
            sharedResources.notifyStateToListener();
        }
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
            startTimerTime = 0; // make sure the retry is not issued
            handleGattDisconnectEvent();
        }
    }

    private void handleGattConnectEvent(int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (shouldWaitUntilBonded()) {
                stateMachine.setState(this, new WaitingUntilBondedState(stateMachine));
            } else {
                stateMachine.setState(this, new DiscoveringServicesState(stateMachine));
            }
        } else {
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
            stateMachine.setState(this, new DisconnectingState(stateMachine));
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        long delta = System.currentTimeMillis() - startTimerTime;

        if (delta < timeOut) {
            sharedResources.setBtGatt(sharedResources.getBtDevice().connectGatt(sharedResources.getShnCentral().getApplicationContext(), false, sharedResources.getShnCentral(), sharedResources.getBTGattCallback()));
        } else {
            stateMachine.setState(this, new DisconnectingState(stateMachine));
        }
    }

    private boolean shouldWaitUntilBonded() {
        return sharedResources.getShnBondInitiator() != SHNDeviceImpl.SHNBondInitiator.NONE && !isBonded();
    }

    public boolean isBonded() {
        return sharedResources.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    private void setMinimumConnectionIdleTime() {
        if(Workaround.EXTENDED_MINIMUM_CONNECTION_IDLE_TIME.isRequiredOnThisDevice()) {
            this.minimumConnectionIdleTime = 2000L;
        } else {
            this.minimumConnectionIdleTime = 1000L;
        }
    }

    private void postponeConnectCall(final boolean withTimeout, final long timeoutInMS, long timeDiff) {
        SHNLogger.w(TAG, "Postponing connect with " + (minimumConnectionIdleTime - timeDiff) + "ms to allow the stack to properly disconnect");

        sharedResources.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startConnect(withTimeout, timeoutInMS);
            }
        }, minimumConnectionIdleTime - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return sharedResources.getLastDisconnectedTimeMillis() != 0L && timeDiff < minimumConnectionIdleTime;
    }
}
