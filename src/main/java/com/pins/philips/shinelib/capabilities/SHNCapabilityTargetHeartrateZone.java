package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityTargetHeartrateZone extends SHNCapability {
    void setTargetZone(short minHeartRate, short maxHeartRate, SHNResultListener shnResultListener);
}
