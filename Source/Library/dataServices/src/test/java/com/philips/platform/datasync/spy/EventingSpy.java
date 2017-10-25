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

public class EventingSpy implements Eventing {
    public Event postedEvent;

    @Override
    public void post(@NonNull final Event event) {
        postedEvent = event;
    }

    @Override
    public void postSticky(@NonNull final Event event) {

    }

    @Override
    public void register(@NonNull final Object subscriber) {

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
