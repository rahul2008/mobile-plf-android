/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasevices.utility;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.Event;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EventingImpl implements Eventing {

    @NonNull
    private final EventBus eventBus;

    @NonNull
    private Handler handler;

    @Inject
    public EventingImpl(@NonNull final EventBus eventBus, @NonNull final Handler handler) {
        this.eventBus = eventBus;
        this.handler = handler;
    }

    @Override
    public void post(@NonNull final Event event) {

        // By forcing post on the main thread, all subscribers that use thread mode BackgroundThread
        // will receive their callbacks on the same background thread.
        // When an event is posted on any other thread, subscribers asking for BackgroundThread will
        // receive the callback on the thread of the caller, instead of the background thread used
        // by posts from the foreground.

        forceMainThreadPostingToGuaranteeReuseOfBackgroundThreadDelivery(event);
    }

    @Override
    public void postSticky(@NonNull final Event event) {
        eventBus.postSticky(event);
    }

    private void forceMainThreadPostingToGuaranteeReuseOfBackgroundThreadDelivery(final @NonNull Event event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                eventBus.post(event);
            }
        });
    }

    @Override
    public void register(@NonNull final Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(@NonNull final Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public boolean isRegistered(@NonNull final Object subscriber) {
        return eventBus.isRegistered(subscriber);
    }

    @Override
    public void registerSticky(@NonNull final Object subscriber) {
        eventBus.registerSticky(subscriber);
    }

    @Override
    public void removeSticky(@NonNull final Event event) {
        eventBus.removeStickyEvent(event);
    }
}
