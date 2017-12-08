package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityClearUserData;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones;

public class SHNCapabilityClearUserDataWrapper implements SHNCapabilityClearUserData {

    private static final String TAG = SHNCapabilityConfigHeartRateZones.class.getSimpleName();
    private final SHNCapabilityClearUserData wrappedShnCapability;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityClearUserDataWrapper(SHNCapabilityClearUserData wrappedShnCapability, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapability = wrappedShnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void clearUserData(@NonNull final SHNResultListener listener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.clearUserData(new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(result);
                            }
                        });
                    }
                });
            }
        });
    }
}
