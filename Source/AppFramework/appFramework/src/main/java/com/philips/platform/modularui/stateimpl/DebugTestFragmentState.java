package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class DebugTestFragmentState extends UIState {
    AppFrameworkApplication appFrameworkApplication;

    public DebugTestFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new DebugTestFragment(), new DebugTestFragment().getClass().getSimpleName());
    }

}
