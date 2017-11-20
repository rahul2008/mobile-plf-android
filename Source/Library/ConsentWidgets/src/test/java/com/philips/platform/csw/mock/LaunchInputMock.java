package com.philips.platform.csw.mock;

import android.content.Context;

import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswLaunchInput;


public class LaunchInputMock extends CswLaunchInput {

    public ContextMock context;

    public LaunchInputMock(ConsentBundleConfig config) {
        super(config, new ContextMock());
        context = new ContextMock();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
