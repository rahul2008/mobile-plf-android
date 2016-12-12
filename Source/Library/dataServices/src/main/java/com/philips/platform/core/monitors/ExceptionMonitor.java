/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.monitors;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.utils.DSLog;

import javax.inject.Inject;

import de.greenrobot.event.SubscriberExceptionEvent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ExceptionMonitor extends EventMonitor {

    public static final String TAG = ExceptionMonitor.class.getSimpleName();

    @NonNull
    private final Handler handler;

    @Inject
    public ExceptionMonitor(@NonNull final Handler handler) {
        this.handler = handler;
    }

    public void onEventMainThread(ExceptionEvent event) {
        int requestId = event.getEventId();
        final String message = event.getMessage();
        final Throwable cause = event.getCause();

        DSLog.v(TAG, "onExceptionEvent (" + requestId + "): " + message + cause);

        handler.post(new Runnable() {
            @Override
            public void run() {
                throw new IllegalStateException(message, cause);
            }
        });
    }

    public void onEventMainThread(final SubscriberExceptionEvent event) {
        Object causingEvent = event.causingEvent;
        final Object causingSubscriber = event.causingSubscriber;

        final String message = "onSubscriberExceptionEvent causingSubscriber(" + causingEvent + ") causingEvent(" + causingSubscriber + ")";
        DSLog.v(TAG, message);

        handler.post(new Runnable() {
            @Override
            public void run() {
                throw new IllegalStateException(message, event.throwable);
            }
        });
    }
}
