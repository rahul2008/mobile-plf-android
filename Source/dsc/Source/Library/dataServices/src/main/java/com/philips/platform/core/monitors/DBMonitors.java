/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;

import java.util.List;

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
