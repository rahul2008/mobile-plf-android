package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.datatypes.SHNUserConfiguration;

/**
 * Created by 310188215 on 02/06/15.
 */
public interface SHNCapabilityUserConfiguration extends SHNCapability {
    void setUserConfiguration(SHNUserConfiguration shnUserConfiguration, SHNResultListener shnResultListener);
}
