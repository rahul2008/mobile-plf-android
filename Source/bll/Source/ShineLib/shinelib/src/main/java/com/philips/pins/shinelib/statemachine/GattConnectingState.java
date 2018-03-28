package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.framework.Timer;

import java.security.InvalidParameterException;

public class GattConnectingState extends State {

    public static final long MINIMUM_CONNECTION_IDLE_TIME = 1000L;
    private static final long CONNECT_TIMEOUT = 20000L;

    private long timeOut;
    private long startTimerTime;
    private long lastDisconnectedTimeMillis; //SET AT DISCONNECT!!

    private Timer connectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            //SHNLogger.e(TAG, "connect timeout in state: " + internalState);
            //failedToConnectResult = SHNResult.SHNErrorTimeout;
            disconnect();
        }
    }, CONNECT_TIMEOUT);

    public GattConnectingState(StateContext context) {
        super(context);
    }

    @Override
    void connect() {
        connect(true, -1L);
    }

    @Override
    void connect(long connectTimeOut) {
        if (connectTimeOut < 0) {
            throw new InvalidParameterException("Time out can not be negative");
        } else {
            this.startTimerTime = System.currentTimeMillis();
            this.timeOut = connectTimeOut;
            connect(true, -1L);
        }
    }

    @Override
    void connect(final boolean withTimeout, final long timeoutInMS) {
        final long timeDiff = System.currentTimeMillis() - lastDisconnectedTimeMillis;
        if (stackNeedsTimeToPrepareForConnect(timeDiff)) {
            postponeConnectCall(withTimeout, timeoutInMS, timeDiff);
            return;
        }

        if (context.getShnCentral().isBluetoothAdapterEnabled()) {
            context.getShnCentral().registerBondStatusListenerForAddress(this, context.getBtDevice().getAddress());
            context.getShnCentral().registerSHNCentralStatusListenerForAddress(this, context.getBtDevice().getAddress());
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
    void disconnect() {
        //connectTimer.stop()
        context.setState(new DisconnectingState(context));
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
        return lastDisconnectedTimeMillis != 0L && timeDiff < MINIMUM_CONNECTION_IDLE_TIME;
    }
}
