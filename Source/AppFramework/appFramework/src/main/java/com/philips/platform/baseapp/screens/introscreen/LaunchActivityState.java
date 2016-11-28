package com.philips.platform.baseapp.screens.introscreen;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by 310240027 on 11/25/2016.
 */

public class LaunchActivityState extends BaseState {
    /**
     * AppFlowState constructor
     *
     */
    public LaunchActivityState() {
        super(AppStates.FIRST_STATE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {

    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
