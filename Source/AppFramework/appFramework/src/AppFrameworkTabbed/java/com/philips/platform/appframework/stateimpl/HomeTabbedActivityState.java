/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.TabbedAppState;
import com.philips.platform.appframework.tabbedscreen.TabDependencies;
import com.philips.platform.appframework.tabbedscreen.TabInterface;
import com.philips.platform.appframework.tabbedscreen.TabLaunchInput;
import com.philips.platform.appframework.tabbedscreen.TabSettings;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeTabbedActivityState extends BaseState {

    private FragmentLauncher fragmentLauncher;

    /**
     * constructor
     */
    public HomeTabbedActivityState() {
        super(TabbedAppState.TAB_HOME);
    }

    /**
     * Navigate to HamburgerActivity
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

    private void launchTabScreen() {
        TabDependencies tabDependencies = new TabDependencies(AppFrameworkApplication.appInfra);
        TabSettings tabSettings = new TabSettings(fragmentLauncher.getFragmentActivity());
        TabLaunchInput tabLaunchInput = new TabLaunchInput();

        TabInterface tabInterface = new TabInterface();
        tabInterface.init(tabDependencies, tabSettings);
        tabInterface.launch(fragmentLauncher, tabLaunchInput);
    }
}
