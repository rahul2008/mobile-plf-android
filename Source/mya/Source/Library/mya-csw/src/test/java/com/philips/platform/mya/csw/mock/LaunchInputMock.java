package com.philips.platform.mya.csw.mock;

import android.content.Context;

import com.philips.platform.mya.csw.CswLaunchInput;
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
