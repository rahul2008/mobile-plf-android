/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

public interface SHNCapabilitySedentary extends SHNCapability {
    void setSedentaryPeriod(short minutes, SHNResultListener shnResultListener);
}
