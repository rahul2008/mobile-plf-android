/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.StreamData;
import com.philips.pins.shinelib.capabilities.StreamIdentifier;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class CapabilityDiCommWrapper implements CapabilityDiComm, ResultListener<StreamData> {

    @NonNull
    private final CapabilityDiComm wrappedShnCapability;

    @NonNull
    private final Handler userHandler;

    @NonNull
    private final Handler internalHandler;

    private Set<ResultListener<StreamData>> streamDataResultListenersSet;

    public CapabilityDiCommWrapper(@NonNull CapabilityDiComm shnCapability, @NonNull Handler internalHandler, @NonNull Handler userHandler) {
        this.wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;

        wrappedShnCapability.addDataListener(this);
        streamDataResultListenersSet = new CopyOnWriteArraySet<>();
    }

    @Override
    public void writeData(@NonNull final byte[] data, final StreamIdentifier streamIdentifier) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.writeData(data, streamIdentifier);
            }
        });
    }

    @Override
    public void addDataListener(@NonNull final ResultListener<StreamData> dataRawResultListener) {
        streamDataResultListenersSet.add(dataRawResultListener);
    }

    @Override
    public void removeDataListener(@NonNull ResultListener<StreamData> dataRawResultListener) {
        streamDataResultListenersSet.remove(dataRawResultListener);
    }

    @Override
    public void onActionCompleted(final StreamData value, @NonNull final SHNResult result) {
        userHandler.post(new Runnable() {
            @Override
            public void run() {
                for (ResultListener<StreamData> dataRawResultListener : streamDataResultListenersSet) {
                    dataRawResultListener.onActionCompleted(value, result);
                }
            }
        });

    }
}
