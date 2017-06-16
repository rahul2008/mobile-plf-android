/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.tabbedscreen.TabDependencies;
import com.philips.platform.appframework.tabbedscreen.TabInterface;
import com.philips.platform.appframework.tabbedscreen.TabLaunchInput;
import com.philips.platform.appframework.tabbedscreen.TabSettings;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeTabbedActivityState extends BaseState {
    public static final String TAG =  HomeTabbedActivityState.class.getSimpleName();

    private FragmentLauncher fragmentLauncher;

    /**
     * constructor
     */
    public HomeTabbedActivityState() {
        super(AppStates.TAB_HOME);
    }

    /**
     * Navigate to HamburgerActivityAbstract
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        launchTabScreen();
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }

    private void launchTabScreen() {
        RALog.d(TAG, " launchTabScreen");
        TabDependencies tabDependencies = new TabDependencies(((AppFrameworkApplication)fragmentLauncher.getFragmentActivity().getApplicationContext()).getAppInfra());
        TabSettings tabSettings = new TabSettings(fragmentLauncher.getFragmentActivity());
        TabLaunchInput tabLaunchInput = new TabLaunchInput();

        TabInterface tabInterface = new TabInterface();
        tabInterface.init(tabDependencies, tabSettings);
        tabInterface.launch(fragmentLauncher, tabLaunchInput);
    }
}
