/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

@SuppressWarnings("WeakerAccess")
public abstract class EventMonitor implements EventingChannel.ChannelCallback {

    protected EventBus eventBus;

    public EventMonitor(@NonNull final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @CallSuper
    @Override
    public void onStart() {
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }
}
