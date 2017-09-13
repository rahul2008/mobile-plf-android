/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.introscreen.welcomefragment;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.flowmanager.utility.UappSharedPreference;
import com.philips.platform.uappdemo.screens.base.UappBasePresenter;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class WelcomeFragmentPresenter extends UappBasePresenter {

    private final int MENU_OPTION_HOME = 0;
    private UappSharedPreference uappSharedPreference;
    private BaseState baseState;
    private UappWelcomeView welcomeFragmentView;
    private String WELCOME_SKIP = "welcome_skip";
    private String WELCOME_DONE = "welcome_done";
    private String WELCOME_HOME = "welcome_home";

    public WelcomeFragmentPresenter(UappWelcomeView welcomeFragmentView) {
        super(welcomeFragmentView);
        this.welcomeFragmentView = welcomeFragmentView;
    }

    @Override
    public void onEvent(final int componentID) {
        String eventState = getEventState(componentID);
        if (eventState.equals(WELCOME_DONE)) {
            uappSharedPreference = new UappSharedPreference(welcomeFragmentView.getFragmentActivity());
            uappSharedPreference.writePreferenceBoolean(UappConstants.DONE_PRESSED, true);
        }
        BaseFlowManager targetFlowManager = welcomeFragmentView.getTargetFlowManager();
        baseState = targetFlowManager.getNextState(eventState);

        if (baseState != null) {
            welcomeFragmentView.showActionBar();
            baseState.navigate(getFragmentLauncher());
            welcomeFragmentView.finishActivity();
        }
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
    }

    // TODO: Deepthi, revisit this switch - (As discussed we need to have this to convert from int ID to string for json)
    protected String getEventState(final int componentID) {

        if (componentID == R.id.ufw_welcome_skip_button) {
            return WELCOME_SKIP;
        } else if (componentID == R.id.ufw_welcome_start_registration_button) {
            return WELCOME_DONE;
        } else if (componentID == MENU_OPTION_HOME) {
            return WELCOME_HOME;
        }
        return WELCOME_HOME;
    }

}
