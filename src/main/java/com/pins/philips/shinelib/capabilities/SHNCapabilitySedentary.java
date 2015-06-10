package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilitySedentary extends SHNCapability {
    void setSedentaryPeriod(short minutes, SHNResultListener shnResultListener); // between 0 and 255
}
