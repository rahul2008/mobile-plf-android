package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityTargets extends SHNCapability {
    void getSupportedTargets(SHNSetResultListener<SHNDataType> shnSetResultListener);
    void setTarget(SHNDataType shnDataType, double target, SHNResultListener shnResultListener);
}
