/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.Date;

public class SHNCapabilityDeviceInformationWrapper implements SHNCapabilityDeviceInformation {
    private static final String TAG = SHNCapabilityDeviceInformationWrapper.class.getSimpleName();
    private static final boolean LOGGING = false;

    @NonNull
    private final SHNCapabilityDeviceInformation wrappedShnCapability;

    @NonNull
    private final Handler userHandler;

    @NonNull
    private final Handler internalHandler;

    public SHNCapabilityDeviceInformationWrapper(@NonNull final SHNCapabilityDeviceInformation shnCapability, @NonNull final Handler internalHandler, @NonNull final Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Deprecated
    @Override
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener) {
        if (LOGGING) Log.i(TAG, "readDeviceInformation called by user");
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.i(TAG, "readDeviceInformation running on device thread");
                wrappedShnCapability.readDeviceInformation(shnDeviceInformationType, new SHNStringResultListener() {
                    @Override
                    public void onActionCompleted(final String value, final SHNResult result) {
                        if (LOGGING)
                            Log.i(TAG, "readDeviceInformation onActionCompleted running on device thread");
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (LOGGING)
                                    Log.i(TAG, "readDeviceInformation onActionCompleted running on user thread");
                                shnStringResultListener.onActionCompleted(value, result);
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
    public void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.readDeviceInformation(deviceInformationType, new Listener() {
                    @Override
                    public void onDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onDeviceInformation(shnDeviceInformationType, value, dateWhenAcquired);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }

                    @Override
                    public void onError(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNResult error) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(shnDeviceInformationType, error);
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
