package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityBluetoothDirect;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

public class CapabilityDiCommWrapper implements CapabilityDiComm {

    @NonNull
    private final CapabilityDiComm wrappedShnCapability;

    @NonNull
    private final Handler userHandler;

    @NonNull
    private final Handler internalHandler;

    @Nullable
    private CapabilityBluetoothDirect.CharacteristicChangedListener characteristicChangedListener;

    public CapabilityDiCommWrapper(@NonNull CapabilityDiComm shnCapability, @NonNull Handler internalHandler, @NonNull Handler userHandler) {
        this.wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void writeData(final byte[] data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.writeData(data);
            }
        };

        internalHandler.post(runnable);
    }

    @Override
    public void addDataListener(final ResultListener<SHNDataRaw> dataRawResultListener) {
        wrappedShnCapability.addDataListener(new ResultListener<SHNDataRaw>() {
            @Override
            public void onActionCompleted(final SHNDataRaw value, @NonNull final SHNResult result) {
                Runnable resultRunnable = new Runnable() {
                    @Override
                    public void run() {
                        dataRawResultListener.onActionCompleted(value, result);
                    }
                };
                userHandler.post(resultRunnable);
            }
        });
    }
}
