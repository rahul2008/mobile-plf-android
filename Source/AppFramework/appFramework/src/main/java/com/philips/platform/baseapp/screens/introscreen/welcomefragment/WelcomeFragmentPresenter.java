/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.userregistration.URStateListener;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class WelcomeFragmentPresenter extends UIBasePresenter implements URStateListener {

    private final int MENU_OPTION_HOME = 0;
    private AppFrameworkApplication appFrameworkApplication;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;
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
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(AppStates.WELCOME, eventState);
        if(baseState!=null) {
            baseState.setStateListener(this);
            if (baseState instanceof UserRegistrationState)
                ((UserRegistrationState) baseState).registerUIStateListener(this);
            fragmentLauncher = getFragmentLauncher();
            baseState.navigate(fragmentLauncher);
        }
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
    }

    // TODO: Deepthi, revisit this switch
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

    // TODO: Deepthi, check for condition and event and then take decision, can we move to json, pls check.
    @Override
    public void onStateComplete(final BaseState baseState) {
        String eventState = getEventState(MENU_OPTION_HOME);
        this.baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.WELCOME, eventState);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.setStateListener(this);
        welcomeFragmentView.finishActivityAffinity();
        this.baseState.navigate(fragmentLauncher);
        if(baseState instanceof UserRegistrationState)
        {
            ((UserRegistrationState) baseState).unregisterUserRegistrationListener();
        }
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
