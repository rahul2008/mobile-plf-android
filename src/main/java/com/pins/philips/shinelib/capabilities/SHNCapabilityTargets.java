package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNSetResultListener;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.datatypes.SHNDataType;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityTargets extends SHNCapability {
    void getSupportedTargets(SHNSetResultListener shnSetResultListener);
    void setTarget(SHNDataType shnDataType, double target, SHNResultListener shnResultListener);
}
