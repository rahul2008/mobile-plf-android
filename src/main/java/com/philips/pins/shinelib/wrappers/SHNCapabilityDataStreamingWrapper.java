package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataStreaming;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Created by 310188215 on 17/05/15.
 */
public class SHNCapabilityDataStreamingWrapper implements SHNCapabilityDataStreaming {
    private final SHNCapabilityDataStreaming wrappedSHNCapabilityDataStreaming;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityDataStreamingWrapper(SHNCapabilityDataStreaming shnCapability, Handler internalHandler, Handler userHandler) {
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        wrappedSHNCapabilityDataStreaming = shnCapability;
    }

    @Override
    public void setStreamingEnabled(final boolean enabled, final SHNDataType shnDataType, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedSHNCapabilityDataStreaming.setStreamingEnabled(enabled, shnDataType, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setShnCapabilityDataStreamingListener(final SHNCapabilityDataStreamingListener shnCapabilityDataStreamingListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedSHNCapabilityDataStreaming.setShnCapabilityDataStreamingListener(shnCapabilityDataStreamingListener);
            }
        };
        internalHandler.post(command);
    }
}
