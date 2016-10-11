/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.monitors;

import android.util.Log;


import com.philips.platform.core.events.Event;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LoggingMonitor extends EventMonitor {

    public static final String TAG = LoggingMonitor.class.getSimpleName();

    @Inject
    public LoggingMonitor() {
    }

    public void onEvent(Event event) {

        StringBuilder builder = new StringBuilder();
        builder.append("onEvent (");
        builder.append(event.getEventId());

        if (event.getReferenceId() > 0) {
            builder.append(") with reference (");
            builder.append(event.getReferenceId());
        }

        builder.append("): ");
        builder.append(event.toString());

        Log.v(TAG, builder.toString());
    }
}
