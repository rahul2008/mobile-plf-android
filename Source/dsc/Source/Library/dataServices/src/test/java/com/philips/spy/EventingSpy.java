/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.spy;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Please use Mockito.verify(), Mockito.when() and Mockito.doAnswer() to replace this.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class EventingSpy implements Eventing {
    public Event postedEvent;
    public DataPushSynchronise registeredClass;
    public Object subscriber;
    public Set<Event> eventBus = new HashSet<>();

    @Override
    public void post(@NonNull final Event event) {
        postedEvent = event;
        eventBus.add(event);
        if (registeredClass != null && registeredClass.getClass().getSimpleName().equals("DataPushSynchronise")) {
            registeredClass.onEventAsync(new GetNonSynchronizedDataResponse(1, new HashMap<Class, List<?>>()));
        }
    }

    @Override
    public void postSticky(@NonNull final Event event) {
    }

    @Override
    public void register(@NonNull final Object subscriber) {
        if (subscriber.getClass().getSimpleName().equals("DataPushSynchronise")) {
            this.registeredClass = (DataPushSynchronise) subscriber;
        }
    }

    @Override
    public void unregister(@NonNull final Object subscriber) {

    }

    @Override
    public boolean isRegistered(@NonNull final Object subscriber) {
        this.subscriber = subscriber;
        return false;
    }

    @Override
    public void removeSticky(@NonNull final Event event) {

    }
}
