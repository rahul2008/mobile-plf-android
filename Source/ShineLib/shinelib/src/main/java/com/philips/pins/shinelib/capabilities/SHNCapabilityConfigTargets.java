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

    class Target {
        private final SHNDataType shnDataType;
        private final short targetValue;

        public Target(final SHNDataType shnDataType, final short targetValue) {
            this.shnDataType = shnDataType;
            this.targetValue = targetValue;
        }

        public SHNDataType getShnDataType() {
            return shnDataType;
        }

        public short getTargetValue() {
            return targetValue;
        }
    }

    void getSupportedDataTypes(ResultListener<Set<SHNDataType>> shnSetResultListener);

    void getTargetForType(SHNDataType type, ResultListener<Target> shnResultListener);

    void setTarget(Target target, ResultListener<Target> shnResultListener);
}
