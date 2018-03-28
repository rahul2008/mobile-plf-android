package com.philips.pins.shinelib.statemachine;

public class DisconnectedState extends State {

    public DisconnectedState(StateContext context) {
        super(context);

        context.setLastDisconnectedTimeMillis(System.currentTimeMillis());

        context.getShnCentral().unregisterSHNCentralStatusListenerForAddress(context.getShnCentralListener(), context.getBtDevice().getAddress());
        context.getShnCentral().unregisterBondStatusListenerForAddress(context.getShnBondStatusListener(), context.getBtDevice().getAddress());
    }

    @Override
    void connect() {
        context.setState(new GattConnectingState(context));
    }

    @Override
    void connect(long connectTimeOut) {
        context.setState(new GattConnectingState(context, connectTimeOut));
    }

    @Override
    void connect(final boolean withTimeout, final long timeoutInMS) {
        context.setState(new GattConnectingState(context, withTimeout, timeoutInMS));
    }
}
