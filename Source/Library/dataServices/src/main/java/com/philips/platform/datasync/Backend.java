/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync;

import com.philips.platform.core.BaseAppBackend;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.monitors.EventMonitor;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Backend implements BaseAppBackend {

    private EventMonitor[] monitors;

    @Inject
    public Backend(final EventMonitor... monitors) {
        this.monitors = monitors;
    }

    @Override
    public void start(final Eventing eventing) {
        for (EventMonitor monitor : monitors) {
            monitor.start(eventing);
        }
    }

    @Override
    public void stop() {
        for (EventMonitor monitor : monitors) {
            monitor.stop();
        }
    }
}
