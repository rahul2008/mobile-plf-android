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
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

import java.util.HashSet;
import java.util.Set;


public class CapabilityDiCommWrapper implements CapabilityDiComm, ResultListener<SHNDataRaw> {

    @NonNull
    private final CapabilityDiComm wrappedShnCapability;

    @NonNull
    private final Handler userHandler;

    @NonNull
    private final Handler internalHandler;

    private Set<ResultListener<SHNDataRaw>> dataRawResultListenersSet;

    public CapabilityDiCommWrapper(@NonNull CapabilityDiComm shnCapability, @NonNull Handler internalHandler, @NonNull Handler userHandler) {
        this.wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;

        wrappedShnCapability.addDataListener(this);
        dataRawResultListenersSet = new HashSet<>();
    }

    @Override
    public void writeData(@NonNull final byte[] data) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.writeData(data);
            }
        });
    }

    @Override
    public void addDataListener(@NonNull final ResultListener<SHNDataRaw> dataRawResultListener) {
        dataRawResultListenersSet.add(dataRawResultListener);
    }

    @Override
    public void removeDataListener(@NonNull ResultListener<SHNDataRaw> dataRawResultListener) {
        dataRawResultListenersSet.remove(dataRawResultListener);
    }

    @Override
    public void onActionCompleted(final SHNDataRaw value, @NonNull final SHNResult result) {
        userHandler.post(new Runnable() {
            @Override
            public void run() {
                for (ResultListener<SHNDataRaw> dataRawResultListener : dataRawResultListenersSet) {
                    dataRawResultListener.onActionCompleted(value, result);
                }
            }
        });

    }
}
