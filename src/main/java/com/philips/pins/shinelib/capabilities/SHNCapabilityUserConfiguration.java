package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNUserConfiguration;

/**
 * Created by 310188215 on 02/06/15.
 */
public interface SHNCapabilityUserConfiguration extends SHNCapability {
    void setUserConfiguration(SHNUserConfiguration shnUserConfiguration, SHNResultListener shnResultListener);
}
