/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;

public abstract class EventMonitor {
    protected Eventing eventing;

    @CallSuper
    public void start(@NonNull final Eventing eventing) {
        this.eventing = eventing;
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    @CallSuper
    public void stop() {
        if (eventing != null) {
            eventing.unregister(this);
            eventing = null;
        }
    }
}
