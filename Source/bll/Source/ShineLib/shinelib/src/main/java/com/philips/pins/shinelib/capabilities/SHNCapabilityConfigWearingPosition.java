/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;

public interface SHNCapabilityConfigWearingPosition extends SHNCapability {

    interface SHNWearingPositionResultListener extends ResultListener<SHNWearingPosition> {
    }

    enum SHNWearingPosition {
        Unknown,
        Wrist,
        LeftWrist,
        RightWrist,
        Pocket,
        Keycord,
        Chest,
        Waist;
    }

    void getSupportedWearingPositions(SHNSetResultListener<SHNWearingPosition> shnSetResultListener);

    void setWearingPosition(SHNWearingPosition shnWearingPosition, SHNResultListener shnResultListener);

    void getWearingPosition(SHNWearingPositionResultListener shnWearingPositionResultListener);
}
