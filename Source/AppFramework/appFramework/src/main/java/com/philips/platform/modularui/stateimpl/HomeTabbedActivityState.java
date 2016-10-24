/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.homescreen.tabbedscreen.TabDependencies;
import com.philips.platform.appframework.homescreen.tabbedscreen.TabInterface;
import com.philips.platform.appframework.homescreen.tabbedscreen.TabLaunchInput;
import com.philips.platform.appframework.homescreen.tabbedscreen.TabSettings;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeTabbedActivityState extends UIState {

    private FragmentLauncher fragmentLauncher;

    /**
     * constructor
     */
    public HomeTabbedActivityState() {
        super(UIState.UI_HOME_TABBED_STATE);
    }

    /**
     * Navigate to HomeActivity
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
//        fragmentLauncher.getFragmentActivity().startActivity(new Intent(fragmentLauncher.getFragmentActivity(), HomeTabbedActivity.class));
        launchTabScreen();
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public AppStates getStateEnum() {
        return AppStates.HOME;
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
