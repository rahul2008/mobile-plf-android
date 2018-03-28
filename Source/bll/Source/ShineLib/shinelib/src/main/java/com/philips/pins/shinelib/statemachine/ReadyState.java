package com.philips.pins.shinelib.statemachine;

public class ReadyState extends State {

    public ReadyState(StateContext context) {
        super(context);
    }

    @Override
    void disconnect() {
        context.setState(new DisconnectingState(context));
    }
}
