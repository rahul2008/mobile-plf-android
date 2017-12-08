package com.philips.platform.core.events;

public class SubjectProfileResponseEvent extends Event{
    private boolean mIsSuccess;

    public SubjectProfileResponseEvent(boolean status){
        mIsSuccess = status;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
