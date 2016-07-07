package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentNavigator implements UIBaseNavigator {
    AppFrameworkApplication appFrameworkApplication;
    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new SettingsFragment(), new SettingsFragment().getClass().getSimpleName());
    }
}
