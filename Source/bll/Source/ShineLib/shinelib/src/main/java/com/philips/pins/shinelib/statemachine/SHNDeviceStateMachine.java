package com.philips.pins.shinelib.statemachine;

public class SHNDeviceStateMachine extends StateMachine<SHNDeviceState> {

    private SHNDeviceResources sharedResources;

    public SHNDeviceStateMachine(SHNDeviceResources sharedResources) {
        super();
        this.sharedResources = sharedResources;
    }

    public SHNDeviceResources getSharedResources() {
        return sharedResources;
    }
}
