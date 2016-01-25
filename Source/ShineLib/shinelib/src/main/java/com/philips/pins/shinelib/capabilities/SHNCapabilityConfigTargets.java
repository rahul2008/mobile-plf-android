/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.datatypes.SHNDataType;

import java.util.Set;

public interface SHNCapabilityConfigTargets extends SHNCapability {

    void getSupportedDataTypes(ResultListener<Set<SHNDataType>> shnSetResultListener);

    void getTargetForType(SHNDataType type, ResultListener<Double> shnResultListener);

    void setTarget(SHNDataType type, double value, ResultListener<Double> shnResultListener);
}
