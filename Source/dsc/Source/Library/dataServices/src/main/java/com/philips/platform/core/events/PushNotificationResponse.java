package com.philips.platform.core.events;

import com.philips.platform.core.listeners.RegisterDeviceTokenListener;

public class PushNotificationResponse extends Event{
    private boolean mIsSuccess;

    public PushNotificationResponse(boolean status) {
        mIsSuccess = status;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
