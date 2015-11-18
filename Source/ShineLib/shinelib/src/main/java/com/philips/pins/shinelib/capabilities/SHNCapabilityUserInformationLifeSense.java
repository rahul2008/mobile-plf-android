/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNStringResultListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SHNCapabilityUserInformationLifeSense extends SHNCapability {
    void getUserNumber(SHNIntegerResultListener shnIntegerResultListener);

    void getBroadCastId(SHNStringResultListener shnStringResultListener);
}
