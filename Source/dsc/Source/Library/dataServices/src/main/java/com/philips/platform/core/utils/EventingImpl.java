/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.utils;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.Event;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
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

 /*   @Override
    public void registerSticky(@NonNull final Object subscriber) {
        eventBus.register(subscriber);
    }*/

    @Override
    public void removeSticky(@NonNull final Event event) {
        eventBus.removeStickyEvent(event);
    }
}