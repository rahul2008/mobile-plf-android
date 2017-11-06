package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SynchronisationCompleteListener;

public class Synchronize extends Event {

    private SynchronisationCompleteListener synchronisationCompleteListener;

    public Synchronize(SynchronisationCompleteListener synchronisationCompleteListener) {
        this.synchronisationCompleteListener = synchronisationCompleteListener;
    }

    public SynchronisationCompleteListener getSynchronisationCompleteListener() {
        return synchronisationCompleteListener;
    }
}
