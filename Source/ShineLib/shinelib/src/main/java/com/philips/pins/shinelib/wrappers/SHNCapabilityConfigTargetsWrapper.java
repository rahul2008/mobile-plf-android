package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigTargets;
import com.philips.pins.shinelib.datatypes.SHNDataType;

import java.util.Set;

public class SHNCapabilityConfigTargetsWrapper implements SHNCapabilityConfigTargets {

    private static final String TAG = SHNCapabilityBatteryWrapper.class.getSimpleName();
    private final SHNCapabilityConfigTargets wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;

    public SHNCapabilityConfigTargetsWrapper(SHNCapabilityConfigTargets wrappedShnCapability, Handler userHandler, Handler internalHandler) {
        this.wrappedShnCapability = wrappedShnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void getSupportedDataTypes(final ResultListener<Set<SHNDataType>> shnSetResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSupportedDataTypes(new ResultListener<Set<SHNDataType>>() {
                    @Override
                    public void onActionCompleted(final Set<SHNDataType> value, @NonNull final SHNResult result) {
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
    public void getTargetForType(final SHNDataType type, final ResultListener<Target> shnResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getTargetForType(type, new ResultListener<Target>() {
                    @Override
                    public void onActionCompleted(final Target value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setTarget(final Target target, final ResultListener<Target> shnResultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setTarget(target, new ResultListener<Target>() {
                    @Override
                    public void onActionCompleted(final Target value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }
}

