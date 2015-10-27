package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;

/**
 * Created by 310188215 on 10/06/15.
 */
public interface SHNCapabilityConfigWearingPosition extends SHNCapability {

    interface SHNWearingPositionResultListener {
        void onActionCompleted(SHNWearingPosition value, SHNResult result);
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
