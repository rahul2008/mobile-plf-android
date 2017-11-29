/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Intent;
import android.os.Bundle;


import com.philips.platform.mya.activity.MyaActivity;

import com.philips.platform.mya.error.MyaError;
import com.philips.platform.mya.injection.DaggerMyaDependencyComponent;
import com.philips.platform.mya.injection.DaggerMyaUiComponent;
import com.philips.platform.mya.injection.MyaDependencyComponent;
import com.philips.platform.mya.injection.MyaDependencyModule;
import com.philips.platform.mya.injection.MyaUiComponent;
import com.philips.platform.mya.injection.MyaUiModule;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserInterface;
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

    private static MyaDependencyComponent myaDependencyComponent;
    private static MyaUiComponent myaUiComponent;
    private MyaUiModule myaUiModule;
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
        UserDataModelProvider userDataModelProvider = new UserDataModelProvider(myaLaunchInput.getContext());
        if(!userDataModelProvider.isUserLoggedIn()) {
            myaLaunchInput.getMyaListener().onError(MyaError.USER_NOT_SIGNED_IN);
            return;
        }
        myaUiModule = new MyaUiModule(uiLauncher, myaLaunchInput.getMyaListener());
        myaUiComponent = DaggerMyaUiComponent.builder()
                .myaUiModule(myaUiModule).build();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_PLUGIN, userDataModelProvider);
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            launchAsActivity(activityLauncher, myaLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, bundle);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, Bundle arguments) {
        myaUiModule.setFragmentLauncher(fragmentLauncher);
        MyaTabFragment myaTabFragment = new MyaTabFragment();
        myaTabFragment.setArguments(arguments);
        myaTabFragment.showFragment(myaTabFragment, fragmentLauncher);
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

        myaDependencyComponent = DaggerMyaDependencyComponent.builder()
                .myaDependencyModule(new MyaDependencyModule(myaDependencies.getAppInfra())).build();
    }


    public static MyaDependencyComponent getMyaDependencyComponent() {
        return myaDependencyComponent;
    }

    public static MyaUiComponent getMyaUiComponent() {
        return myaUiComponent;
    }

    public static void setMyaUiComponent(MyaUiComponent myaUiComponent) {
        MyaInterface.myaUiComponent = myaUiComponent;
    }
}
