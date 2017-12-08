/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;

/**
 * A capability that correctly handles threading.
 * <p>
 * All calls into Bluelib should be handled on the internalHandler, all calls back
 * to the end user should be done on the userHandler. These handlers are provided to
 * the capability when it is added to a SHNDevice.
 */
public interface SHNCapabilityThreadSafe extends SHNCapability {

    void setHandlers(Handler internalHandler, Handler userHandler);
}
