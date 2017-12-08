/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.activity.MyaActivity;
import com.philips.platform.mya.error.MyaError;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.platform.mya.MyaConstants.MYA_DLS_THEME;


public class MyaInterface implements UappInterface {

    public static String USER_PLUGIN = "user_plugin";
    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        MyaLaunchInput myaLaunchInput = (MyaLaunchInput) uappLaunchInput;
        UserDataModelProvider userDataModelProvider = getUserDataModelProvider(myaLaunchInput);
        if (!userDataModelProvider.isUserLoggedIn(myaLaunchInput.getContext())) {
            myaLaunchInput.getMyaListener().onError(MyaError.USER_NOT_SIGNED_IN);
            return;
        }
        MyaHelper.getInstance().setMyaListener(myaLaunchInput.getMyaListener());
        MyaHelper.getInstance().setMyaLaunchInput(myaLaunchInput);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_PLUGIN, userDataModelProvider);
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            MyaHelper.getInstance().setThemeConfiguration(activityLauncher.getDlsThemeConfiguration());
            launchAsActivity(activityLauncher, myaLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, bundle);
        }
    }

    public UserDataModelProvider getUserDataModelProvider(MyaLaunchInput myaLaunchInput) {
        return new UserDataModelProvider(myaLaunchInput.getContext());
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, Bundle arguments) {
        MyaTabFragment myaTabFragment = new MyaTabFragment();
        myaTabFragment.setArguments(arguments);
        myaTabFragment.setFragmentLauncher(fragmentLauncher);
        myaTabFragment.setActionbarUpdateListener(fragmentLauncher.getActionbarListener());
        myaTabFragment.showFragment(myaTabFragment);
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, MyaLaunchInput myaLaunchInput) {
        if (null != uiLauncher && myaLaunchInput != null) {
            Intent myAccountIntent = new Intent(myaLaunchInput.getContext(), MyaActivity.class);
            myAccountIntent.putExtra(MYA_DLS_THEME, uiLauncher.getUiKitTheme());
            myaLaunchInput.getContext().startActivity(myAccountIntent);
        }
    }

    /**
     * Entry point for MyAccount. Please make sure no User registration components are being used before MyaInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        MyaDependencies myaDependencies = (MyaDependencies) uappDependencies;
        MyaHelper.getInstance().setAppInfra(myaDependencies.getAppInfra());
    }

}