package com.philips.platform.mya.csw.mock;

import android.content.Context;

import com.philips.platform.mya.csw.CswLaunchInput;


public class LaunchInputMock extends CswLaunchInput {

    public ContextMock context;

    public LaunchInputMock() {
        super(new ContextMock());
        context = (ContextMock) super.getContext();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
