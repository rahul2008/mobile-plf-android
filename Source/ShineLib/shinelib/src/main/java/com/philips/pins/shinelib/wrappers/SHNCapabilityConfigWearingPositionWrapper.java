/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigWearingPosition;

import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityConfigWearingPositionWrapper implements SHNCapabilityConfigWearingPosition {
    private final SHNCapabilityConfigWearingPosition wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;

    public SHNCapabilityConfigWearingPositionWrapper(SHNCapabilityConfigWearingPosition shnCapability, Handler internalHandler, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void getSupportedWearingPositions(final SHNSetResultListener<SHNWearingPosition> shnSetResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSupportedWearingPositions(new SHNSetResultListener<SHNWearingPosition>() {
                    @Override
                    public void onActionCompleted(final Set<SHNWearingPosition> value, final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnSetResultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setWearingPosition(final SHNWearingPosition shnWearingPosition, final SHNResultListener shnResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setWearingPosition(shnWearingPosition, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void getWearingPosition(final SHNWearingPositionResultListener shnWearingPositionResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getWearingPosition(new SHNWearingPositionResultListener() {

                    @Override
                    public void onActionCompleted(final SHNWearingPosition value, final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnWearingPositionResultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }
}
