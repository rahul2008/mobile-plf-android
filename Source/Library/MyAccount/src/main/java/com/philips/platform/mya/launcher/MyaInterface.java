/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.activity.MyAccountActivity;
import com.philips.platform.mya.MyaUiHelper;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.platform.mya.util.MyaConstants.MYA_DLS_THEME;

public class MyaInterface implements UappInterface {

    private MyaUiHelper myaUiHelper;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        myaUiHelper = MyaUiHelper.getInstance();
        myaUiHelper.setAppInfra((AppInfra) uappDependencies.getAppInfra());
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        MyaLaunchInput myaLaunchInput = (MyaLaunchInput) uappLaunchInput;
        FragmentActivity context = (FragmentActivity) myaLaunchInput.getContext();
        myaUiHelper.setMyaListener(myaLaunchInput.getMyaListener());
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, MyAccountActivity.class);
            Bundle bundle = new Bundle();

            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            bundle.putInt(MYA_DLS_THEME, activityLauncher.getUiKitTheme());
            myaUiHelper.setThemeConfiguration(activityLauncher.getDlsThemeConfiguration());
            intent.putExtras(bundle);
            context.startActivity(intent);
        } else {
            MyaTabFragment myaTabFragment = new MyaTabFragment();
            myaUiHelper.setFragmentLauncher((FragmentLauncher)uiLauncher);
            myaTabFragment.showFragment(myaTabFragment, (FragmentLauncher)uiLauncher);
        }
    }

}
