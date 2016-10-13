package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DBMonitors {
    @NonNull
    private final List<EventMonitor> eventMonitors;

    public DBMonitors(@NonNull List<EventMonitor> monitors) {
        this.eventMonitors = monitors;
    }

    public void start(@NonNull final Eventing eventing) {
        for (EventMonitor monitor : eventMonitors) {
            monitor.start(eventing);
        }
    }

    public void stop() {
        for (EventMonitor monitor : eventMonitors) {
            monitor.stop();
        }
    }
}
