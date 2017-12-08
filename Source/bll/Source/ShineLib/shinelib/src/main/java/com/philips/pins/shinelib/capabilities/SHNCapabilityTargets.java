/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataType;

public interface SHNCapabilityTargets extends SHNCapability {
    void getSupportedTargets(SHNSetResultListener<SHNDataType> shnSetResultListener);
    void setTarget(SHNDataType shnDataType, double target, SHNResultListener shnResultListener);
}
