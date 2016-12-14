/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class WelcomeFragmentPresenter extends UIBasePresenter{

    private final int MENU_OPTION_HOME = 0;
    private AppFrameworkApplication appFrameworkApplication;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private BaseState baseState;
    private WelcomeFragmentView welcomeFragmentView;
    private String WELCOME_SKIP = "welcome_skip";
    private String WELCOME_DONE = "welcome_done";
    private String WELCOME_HOME = "welcome_home";

    public WelcomeFragmentPresenter(WelcomeFragmentView welcomeFragmentView) {
        super(welcomeFragmentView);
        this.welcomeFragmentView = welcomeFragmentView;
    }

    @Override
    public void onEvent(final int componentID) {
        appFrameworkApplication = getApplicationContext();
        welcomeFragmentView.showActionBar();
        String eventState = getEventState(componentID);
        if (eventState.equals(WELCOME_DONE)) {
            sharedPreferenceUtility = new SharedPreferenceUtility(welcomeFragmentView.getFragmentActivity());
            sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
        }
        BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
        baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), eventState);
        if(baseState!=null) {
            baseState.navigate(getFragmentLauncher());
        }
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
    }

    // TODO: Deepthi, revisit this switch - (As discussed we need to have this to convert from int ID to string for json)
    protected String getEventState(final int componentID) {
        switch (componentID) {
            case R.id.welcome_skip_button:
                return WELCOME_SKIP;
            case R.id.welcome_start_registration_button:
                return WELCOME_DONE;
            case MENU_OPTION_HOME:
                return WELCOME_HOME;
        }
        return WELCOME_HOME;
    }

}
