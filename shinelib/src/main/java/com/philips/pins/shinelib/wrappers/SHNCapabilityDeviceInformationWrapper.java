package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

/**
 * Created by 310188215 on 29/04/15.
 */
public class SHNCapabilityDeviceInformationWrapper implements SHNCapabilityDeviceInformation {
    private static final String TAG = SHNCapabilityDeviceInformationWrapper.class.getSimpleName();
    private static final boolean LOGGING = false;
    private final SHNCapabilityDeviceInformation wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;

    public SHNCapabilityDeviceInformationWrapper(SHNCapabilityDeviceInformation shnCapability, Handler internalHandler, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    // implements SHNCapabilityDeviceInformation
    @Override
    public void readDeviceInformation(final SHNDeviceInformationType shnDeviceInformationType, final SHNStringResultListener shnStringResultListener) {
        if (LOGGING) Log.i(TAG, "readDeviceInformation called by user");
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.i(TAG, "readDeviceInformation running on device thread");
                wrappedShnCapability.readDeviceInformation(shnDeviceInformationType, new SHNStringResultListener() {
                    @Override
                    public void onActionCompleted(final String value, final SHNResult result) {
                        if (LOGGING) Log.i(TAG, "readDeviceInformation onActionCompleted running on device thread");
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (LOGGING) Log.i(TAG, "readDeviceInformation onActionCompleted running on user thread");
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
}
