/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataStreaming;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;

import java.util.Set;

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
    public void getSupportedDataTypes(final SHNSetResultListener<SHNDataType> shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedSHNCapabilityDataStreaming.getSupportedDataTypes(new SHNSetResultListener<SHNDataType>() {
                    @Override
                    public void onActionCompleted(final Set<SHNDataType> value, final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(value, result);
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
    public void setDataListener(final SHNCapabilityDataStreamingListener listener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedSHNCapabilityDataStreaming.setDataListener(new SHNCapabilityDataStreamingListener() {
                    @Override
                    public void onReceiveData(final SHNData data) {
                        Runnable dataCallbackRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onReceiveData(data);
                            }
                        };
                        userHandler.post(dataCallbackRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
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
}
