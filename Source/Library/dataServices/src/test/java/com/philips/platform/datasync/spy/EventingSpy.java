/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.spy;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;

import java.util.HashMap;
import java.util.List;

public class EventingSpy implements Eventing {
    public Event postedEvent;
    public DataPushSynchronise registeredClass;

    @Override
    public void post(@NonNull final Event event) {
        postedEvent = event;
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
        return false;
    }

    @Override
    public void removeSticky(@NonNull final Event event) {

    }
}
