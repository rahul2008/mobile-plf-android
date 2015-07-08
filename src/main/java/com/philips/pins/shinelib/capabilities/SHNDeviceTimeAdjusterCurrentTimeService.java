package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.services.SHNServiceCurrentTime;
import com.philips.pins.shinelib.utility.ExactTime256WithAdjustReason;

/**
 * Created by 310188215 on 08/07/15.
 */
public class SHNDeviceTimeAdjusterCurrentTimeService implements SHNDeviceTimeAdjuster {
    private long timeDelta;
    private final SHNServiceCurrentTime shnServiceCurrentTime;
    private SHNService.State adjusterServiceState = SHNService.State.Unavailable;

    private final SHNServiceCurrentTime.SHNServiceCurrentTimeListener shnServiceCurrentTimeListener =  new SHNServiceCurrentTime.SHNServiceCurrentTimeListener() {
        @Override
        public void onServiceStateChanged(final SHNServiceCurrentTime shnServiceCurrentTime, SHNService.State state) {
            if (SHNService.State.Available == state && SHNService.State.Available != adjusterServiceState) {
                shnServiceCurrentTime.getCurrentTime(new SHNObjectResultListener() {
                    @Override
                    public void onActionCompleted(Object object, SHNResult result) {
                        if (SHNResult.SHNOk == result) {
                            determineTimeDeltaBetweenDeviceTimeAndHostTime((ExactTime256WithAdjustReason) object);
                            shnServiceCurrentTime.transitionToReady();
                        } else {
                            shnServiceCurrentTime.transitionToError();
                        }
                    }
                });
            }
            adjusterServiceState = state;
        }
    };

    private void determineTimeDeltaBetweenDeviceTimeAndHostTime(ExactTime256WithAdjustReason exactTime256WithAdjustReason) {
        long deviceTime = exactTime256WithAdjustReason.exactTime256.exactTime256Date.getTime();
        long systemTime = System.currentTimeMillis();
        timeDelta = systemTime - deviceTime;
    }

    public SHNDeviceTimeAdjusterCurrentTimeService(SHNServiceCurrentTime shnServiceCurrentTime) {
        this.shnServiceCurrentTime = shnServiceCurrentTime;
        shnServiceCurrentTime.setSHNServiceCurrentTimeListener(shnServiceCurrentTimeListener);
    }

    // implements DeviceTimeAdjuster
    @Override
    public long adjustTimestampToHostTime(long timestamp) {
        long adjustedTime = timestamp + timeDelta;
        return adjustedTime;
    }
}

