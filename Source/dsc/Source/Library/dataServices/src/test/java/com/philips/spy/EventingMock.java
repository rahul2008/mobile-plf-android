package com.philips.spy;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.Event;

public class EventingMock implements Eventing {

    public Event postedEvent;

    @Override
    public void post(@NonNull Event event) {
        this.postedEvent = event;
    }

    @Override
    public void postSticky(@NonNull Event event) {

    }

    @Override
    public void register(@NonNull Object subscriber) {

    }

    @Override
    public void unregister(@NonNull Object subscriber) {

    }

    @Override
    public boolean isRegistered(@NonNull Object subscriber) {
        return false;
    }

    @Override
    public void removeSticky(@NonNull Event event) {

    }
}
