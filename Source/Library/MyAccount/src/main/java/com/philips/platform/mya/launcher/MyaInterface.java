/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Intent;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswSettings;
import com.philips.platform.mya.activity.MyAccountActivity;
import com.philips.platform.mya.injection.DaggerMyaDependencyComponent;
import com.philips.platform.mya.injection.DaggerMyaUiComponent;
import com.philips.platform.mya.injection.MyaDependencyComponent;
import com.philips.platform.mya.injection.MyaDependencyModule;
import com.philips.platform.mya.injection.MyaUiComponent;
import com.philips.platform.mya.injection.MyaUiModule;
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

    private static String applicationName;
    private static String propositionName;
    private static MyaDependencyComponent myaDependencyComponent;
    private static MyaUiComponent myaUiComponent;

    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        MyaLaunchInput myaLaunchInput = (MyaLaunchInput) uappLaunchInput;
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity((ActivityLauncher) uiLauncher, myaLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, myaLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, MyaLaunchInput myaLaunchInput) {
        myaUiComponent = DaggerMyaUiComponent.builder()
                .myaUiModule(new MyaUiModule(fragmentLauncher, myaLaunchInput.getMyaListener(), null)).build();
        MyaTabFragment myaTabFragment = new MyaTabFragment();
        myaTabFragment.showFragment(myaTabFragment, fragmentLauncher);
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, MyaLaunchInput myaLaunchInput) {
        if (null != uiLauncher && myaLaunchInput != null) {
            Intent myAccountIntent = new Intent(myaLaunchInput.getContext(), MyAccountActivity.class);
            myAccountIntent.putExtra(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
            myAccountIntent.putExtra(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
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
        CswDependencies cswDependencies = new CswDependencies(uappDependencies.getAppInfra());
        MyaDependencies myaDependencies = (MyaDependencies) uappDependencies;
        applicationName = (myaDependencies).getApplicationName();
        propositionName = (myaDependencies).getPropositionName();
        cswDependencies.setApplicationName(applicationName == null ? CatkConstants.APPLICATION_NAME : applicationName);
        cswDependencies.setPropositionName(propositionName == null ? CatkConstants.PROPOSITION_NAME : propositionName);
        CswSettings cswSettings = new CswSettings(uappSettings.getContext());
        CswInterface cswInterface = new CswInterface();
        cswInterface.init(cswDependencies, cswSettings);
        myaDependencyComponent = DaggerMyaDependencyComponent.builder()
                .myaDependencyModule(new MyaDependencyModule((AppInfra) myaDependencies.getAppInfra())).build();
    }


    public static MyaDependencyComponent getMyaDependencyComponent() {
        return myaDependencyComponent;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static String getPropositionName() {
        return propositionName;
    }

    public static MyaUiComponent getMyaUiComponent() {
        return myaUiComponent;
    }
}
