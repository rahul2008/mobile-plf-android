package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentNavigator implements UIBaseNavigator {
    @Override
    public void loadActivity(Context context) {

    }

    @Override
    public Fragment loadFragment() {
        return new SettingsFragment();
    }
}
