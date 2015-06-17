package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;

/**
 * Created by 310188215 on 17/06/15.
 */
public class SHNCapabilityHealthThermometerLogSync implements SHNCapabilityLogSynchronization {

    private final SHNServiceHealthThermometer shnServiceHealthThermometer;
    private State state;

    public SHNCapabilityHealthThermometerLogSync(SHNServiceHealthThermometer shnServiceHealthThermometer) {
        state = State.Idle;
        this.shnServiceHealthThermometer = shnServiceHealthThermometer;
    }

    // implements SHNCapabilityLogSynchronization
    @Override
    public void setListener(Listener listener) {

    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Object getLastSynchronizationToken() {
        return new Object();
    }

    @Override
    public void startSynchronizationFromToken(Object synchronizationToken) {

    }

    @Override
    public void abortSynchronization() {

    }

    @Override
    public void getValueForOption(Option option, SHNIntegerResultListener shnResultListener) {

    }

    @Override
    public void setValueForOption(int value, Option option, SHNResultListener shnResultListener) {

    }
}
