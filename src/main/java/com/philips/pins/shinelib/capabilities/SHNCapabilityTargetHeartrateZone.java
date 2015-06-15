package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityTargetHeartrateZone extends SHNCapability {
    void setTargetZone(short minHeartRate, short maxHeartRate, SHNResultListener shnResultListener);
}
