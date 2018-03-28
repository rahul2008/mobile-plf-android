package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNDevice;

public class DisconnectedState extends State {

    public DisconnectedState(StateContext context) {
        super(context);

        context.setLastDisconnectedTimeMillis(System.currentTimeMillis());

        context.getShnCentral().unregisterSHNCentralStatusListenerForAddress(context.getShnCentralListener(), context.getBtDevice().getAddress());
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Disconnected;
    }

    @Override
    public void connect() {
        context.setState(new GattConnectingState(context));
    }

    @Override
    public void connect(long connectTimeOut) {
        context.setState(new GattConnectingState(context, connectTimeOut));
    }

    @Override
    public void connect(final boolean withTimeout, final long timeoutInMS) {
        context.setState(new GattConnectingState(context, withTimeout, timeoutInMS));
    }
}
