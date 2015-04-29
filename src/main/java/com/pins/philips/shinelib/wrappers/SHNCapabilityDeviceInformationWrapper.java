package com.pins.philips.shinelib.wrappers;

import android.os.Handler;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.SHNStringResultListener;
import com.pins.philips.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by 310188215 on 29/04/15.
 */
public class SHNCapabilityDeviceInformationWrapper implements SHNCapabilityDeviceInformation {
    private final SHNCapabilityDeviceInformation wrappedShnCapability;
    private final Handler userHandler;
    private final ScheduledThreadPoolExecutor shnExecutor;

    public SHNCapabilityDeviceInformationWrapper(SHNCapabilityDeviceInformation shnCapability, ScheduledThreadPoolExecutor shnExecutor, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.shnExecutor = shnExecutor;
    }

    // implements SHNCapabilityDeviceInformation
    @Override
    public void readDeviceInformation(final SHNDeviceInformationType shnDeviceInformationType, final SHNStringResultListener shnStringResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.readDeviceInformation(shnDeviceInformationType, new SHNStringResultListener() {
                    @Override
                    public void onActionCompleted(final String value, final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnStringResultListener.onActionCompleted(value, result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        shnExecutor.execute(command);
    }
}
