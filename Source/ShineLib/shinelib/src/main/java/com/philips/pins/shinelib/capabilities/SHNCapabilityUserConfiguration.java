/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNUserConfiguration;

/**
 * Interface to set user specific information to a peripheral. This interface is deprecated. Please
 * refer to {@link SHNCentral#getSHNUserConfiguration()} and {@link SHNUserConfiguration}.
 */
@Deprecated
public interface SHNCapabilityUserConfiguration extends SHNCapability {
    /**
     * Sets the specified user data to the peripheral.
     *
     * @param shnUserConfiguration user information
     * @param shnResultListener    result of the set operation
     */
    void setUserConfiguration(SHNUserConfiguration shnUserConfiguration, SHNResultListener shnResultListener);
}
