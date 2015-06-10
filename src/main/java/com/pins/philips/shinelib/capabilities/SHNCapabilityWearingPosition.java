package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.SHNSetResultListener;
import com.pins.philips.shinelib.SHNStringResultListener;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityWearingPosition extends SHNCapability {
    public enum  SHNWearingPosition {
        Wrist,
        Pocket,
        Keycord,
        Chest,
        Waist        
    }

    void getSupportedWearingPositions(SHNSetResultListener<SHNWearingPosition> shnSetResultListener);
    void setWearingPosition(SHNWearingPosition shnWearingPosition, SHNResultListener shnResultListener);
}
