package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;

import java.security.InvalidParameterException;

public class GattConnectingState extends State {

    public static final long MINIMUM_CONNECTION_IDLE_TIME = 1000L;
    private static final long CONNECT_TIMEOUT = 20000L;

    private long timeOut;
    private long startTimerTime;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            //SHNLogger.e(TAG, "connect timeout in state: " + internalState);
            //failedToConnectResult = SHNResult.SHNErrorTimeout;
            disconnect();
        }
    }, CONNECT_TIMEOUT);

    public GattConnectingState(StateContext context) {
        this(context, true, -1L);
    }

    public GattConnectingState(StateContext context, long connectTimeOut) {
        super(context);

        if (connectTimeOut < 0) {
            throw new InvalidParameterException("Time out can not be negative");
        } else {
            this.startTimerTime = System.currentTimeMillis();
            this.timeOut = connectTimeOut;
            startConnect(true, -1L);
        }
    }

    public GattConnectingState(StateContext context, final boolean withTimeout, final long timeoutInMS) {
        super(context);

        startConnect(withTimeout, timeoutInMS);
    }

    @Override
    void disconnect() {
        connectTimer.stop();
        context.setState(new DisconnectingState(context));
    }

    private void startConnect(final boolean withTimeout, final long timeoutInMS) {
        final long timeDiff = System.currentTimeMillis() - context.getLastDisconnectedTimeMillis();
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(withTimeout, timeoutInMS, timeDiff);
            return;
        }

        if (context.getShnCentral().isBluetoothAdapterEnabled()) {
            context.getShnCentral().registerBondStatusListenerForAddress(context.getShnBondStatusListener(), context.getBtDevice().getAddress());
            context.getShnCentral().registerSHNCentralStatusListenerForAddress(context.getShnCentralListener(), context.getBtDevice().getAddress());
            if (withTimeout) {
                if (timeoutInMS > 0) {
                    connectTimer.setTimeoutForSubsequentRestartsInMS(timeoutInMS);
                }
                context.setBtGatt(context.getBtDevice().connectGatt(context.getShnCentral().getApplicationContext(), false, context.getShnCentral(), context.getBTGattCallback()));
            } else {
                context.setBtGatt(context.getBtDevice().connectGatt(context.getShnCentral().getApplicationContext(), true, context.getShnCentral(), context.getBTGattCallback()));
            }
        } else {
            //notifyStateToListener();
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
        connectTimer.stop();

        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (shouldWaitUntilBonded()) {
                context.setState(new WaitingUntilBondedState(context));
            } else {
                context.setState(new DiscoveringServicesState(context));
            }
        } else {
            //failedToConnectResult = SHNResult.SHNErrorConnectionLost;
            //setInternalStateReportStateUpdateAndSetTimers(SHNDeviceImpl2.InternalState.Disconnecting);
            context.setState(new DisconnectingState(context));
        }
    }

    private void handleGattDisconnectEvent() {
        context.getBtGatt().close();
        context.setBtGatt(null);

        long delta = System.currentTimeMillis() - startTimerTime;

        if (delta < timeOut) {
            context.setBtGatt(context.getBtDevice().connectGatt(context.getShnCentral().getApplicationContext(), false, context.getShnCentral(), context.getBTGattCallback()));
        } else {
            connectTimer.stop();
            context.setState(new DisconnectingState(context));
        }
    }

    private boolean shouldWaitUntilBonded() {
        return context.getShnBondInitiator() != SHNDeviceImpl.SHNBondInitiator.NONE && !isBonded();
    }

    public boolean isBonded() {
        return context.getBtDevice().getBondState() == BluetoothDevice.BOND_BONDED;
    }

    private void postponeConnectCall(final boolean withTimeout, final long timeoutInMS, long timeDiff) {
        context.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connect(withTimeout, timeoutInMS);
            }
        }, MINIMUM_CONNECTION_IDLE_TIME - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return context.getLastDisconnectedTimeMillis() != 0L && timeDiff < MINIMUM_CONNECTION_IDLE_TIME;
    }
}
