package com.philips.platform.csw.mock;

import android.content.Context;

import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;


public class LaunchInputMock extends CswLaunchInput {

    public ContextMock context;

    public LaunchInputMock() {
        super(new ContextMock(), new ArrayList<ConsentDefinition>());
        context = (ContextMock) super.getContext();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
