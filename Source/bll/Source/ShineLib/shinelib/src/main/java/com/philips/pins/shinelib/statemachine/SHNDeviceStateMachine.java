package com.philips.pins.shinelib.statemachine;

public class SHNDeviceStateMachine extends StateMachine<SHNDeviceState> {

    private SHNDeviceResources sharedResources;

    public SHNDeviceStateMachine(StateChangedListener<SHNDeviceState> stateChangedListener, SHNDeviceResources sharedResources) {
        super(stateChangedListener);
        this.sharedResources = sharedResources;
    }

    public SHNDeviceResources getSharedResources() {
        return sharedResources;
    }
}
