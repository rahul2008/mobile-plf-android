package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigTargets;
import com.philips.pins.shinelib.datatypes.SHNDataType;

import java.util.Set;

public class SHNCapabilityConfigTargetsWrapper implements SHNCapabilityConfigTargets {

    private static final String TAG = SHNCapabilityConfigTargets.class.getSimpleName();
    private final SHNCapabilityConfigTargets wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;

    public SHNCapabilityConfigTargetsWrapper(SHNCapabilityConfigTargets wrappedShnCapability, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapability = wrappedShnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void getSupportedDataTypes(final ResultListener<Set<SHNDataType>> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSupportedDataTypes(new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }

    @Override
    public void getTargetForType(final SHNDataType type, final ResultListener<Double> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getTargetForType(type, new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }

    @Override
    public void setTarget(final SHNDataType type, final double value, final ResultListener<Double> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setTarget(type, value, new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }
}

