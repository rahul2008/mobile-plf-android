/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class WelcomeState extends UIState {

    private FragmentLauncher fragmentLauncher;
    /**
     * constructor
     * @param stateID
     */
    public WelcomeState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * to navigate
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        fragmentLauncher.getFragmentActivity().startActivity(new Intent(fragmentLauncher.getFragmentActivity(), WelcomeActivity.class));
    }

    /**
     * handles handleBack pressed
     * @param context requires context
     */
    @Override
    public void handleBack(final Context context) {
    }

    @Override
    public void init(Context context) {

    }
}
