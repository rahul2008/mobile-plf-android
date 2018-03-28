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

    private long timeOut;
    private long startTimerTime;
    private long minimumConnectionIdleTime;

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            context.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            disconnect();
        }
    }, CONNECT_TIMEOUT);

    public GattConnectingState(StateContext context) {
        this(context, true, -1L);
    }

    public GattConnectingState(StateContext context, long connectTimeOut) {
        super(context);

        setMinimumConnectionIdleTime();

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

        setMinimumConnectionIdleTime();

        startConnect(withTimeout, timeoutInMS);
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void disconnect() {
        SHNLogger.w(TAG, "to disconnect state");
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
            context.notifyStateToListener();
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
            context.notifyFailureToListener(SHNResult.SHNErrorConnectionLost);
            context.setState(new DisconnectingState(context));
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = context.getBtGatt();
        if(btGatt != null) {
            btGatt.close();
        }
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

    private void setMinimumConnectionIdleTime() {
        if(Workaround.EXTENDED_MINIMUM_CONNECTION_IDLE_TIME.isRequiredOnThisDevice()) {
            this.minimumConnectionIdleTime = 2000L;
        } else {
            this.minimumConnectionIdleTime = 1000L;
        }
    }

    private void postponeConnectCall(final boolean withTimeout, final long timeoutInMS, long timeDiff) {
        SHNLogger.w(TAG, "Postponing connect with " + (minimumConnectionIdleTime - timeDiff) + "ms to allow the stack to properly disconnect");

        context.getShnCentral().getInternalHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startConnect(withTimeout, timeoutInMS);
            }
        }, minimumConnectionIdleTime - timeDiff);
    }

    private boolean stackNeedsTimeToPrepareForConnect(long timeDiff) {
        return context.getLastDisconnectedTimeMillis() != 0L && timeDiff < minimumConnectionIdleTime;
    }
}
