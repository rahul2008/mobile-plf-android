package com.philips.platform.core.events;

public class DevicePairingResponseEvent extends Event{
    private boolean mIsSuccess;

    public DevicePairingResponseEvent(boolean status){
        mIsSuccess = status;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
