/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.navigation;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.UappStates;
import com.philips.platform.uappframework.launcher.UiLauncher;


public class NavigationExpState extends BaseState {

    public NavigationExpState() {
        super(UappStates.NAVIGATION_IMPL);
    }

    /**
     * to navigate
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        NavigationImplMicroAppInterface navigationImplMicroAppInterface = new NavigationImplMicroAppInterface();
        navigationImplMicroAppInterface.launch(uiLauncher, null);
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void updateDataModel() {

    }
}
