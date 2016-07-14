package com.philips.platform.appframework.settingscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentPresenter implements UIBasePresenter {
    AppFrameworkApplication appFrameworkApplication;;
    @Override
    public void onClick(int componentID, Context context) {
    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateState(UIConstants.UI_REGISTRATION_STATE, context);
    }
}
