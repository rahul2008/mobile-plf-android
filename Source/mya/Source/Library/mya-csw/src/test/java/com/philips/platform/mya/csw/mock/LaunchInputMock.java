package com.philips.platform.mya.csw.mock;

import com.philips.platform.mya.csw.CswLaunchInput;

import android.content.Context;


public class LaunchInputMock extends CswLaunchInput {

    public ContextMock context;

    public LaunchInputMock() {
        super(new ContextMock(), "http://google.com");
        context = (ContextMock)super.getContext();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
