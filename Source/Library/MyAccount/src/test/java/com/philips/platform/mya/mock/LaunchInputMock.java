package com.philips.platform.mya.mock;

import android.content.Context;

import com.philips.platform.mya.MyaLaunchInput;


public class LaunchInputMock extends MyaLaunchInput {

    public ContextMock context;

    public LaunchInputMock() {
        context = new ContextMock();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
