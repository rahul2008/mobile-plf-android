package com.philips.pins.shinelib;

import com.philips.pins.shinelib.capabilities.SHNCapabilityWearingPosition;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNWearingPositionResultListener {
    void onActionCompleted(SHNCapabilityWearingPosition.SHNWearingPosition value, SHNResult result);
}
