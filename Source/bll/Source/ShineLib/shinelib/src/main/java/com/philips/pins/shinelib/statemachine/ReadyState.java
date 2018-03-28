package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNDevice;

public class ReadyState extends State {

    public ReadyState(StateContext context) {
        super(context);
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connected;
    }

    @Override
    public void disconnect() {
        context.setState(new DisconnectingState(context));
    }
}
