/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class EventingChannel<T extends EventingChannel.ChannelCallback> {

    @SuppressWarnings("WeakerAccess")
    public interface ChannelCallback {
        void onStart();

        void onStop();
    }

    private final List<T> eventingChannels;

    @Inject
    public EventingChannel(@NonNull final List<T> eventingChannels) {
        this.eventingChannels = eventingChannels;
    }

    public void start() {
        for (T channel : eventingChannels) {
            channel.onStart();
        }
    }

    public void stop() {
        for (T channel : eventingChannels) {
            channel.onStop();
        }
    }
}
