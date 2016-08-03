package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentState extends UIState {
    UICoCoInterface uiCoCoUserReg;

    public SettingsFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new SettingsFragment(), new SettingsFragment().getClass().getSimpleName());
    }
}
