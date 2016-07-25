package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.homescreen.HomeFragment;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class HomeFragmentState extends UIState {
    AppFrameworkApplication appFrameworkApplication;

    public HomeFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new HomeFragment(), new HomeFragment().getClass().getSimpleName());
    }
}
