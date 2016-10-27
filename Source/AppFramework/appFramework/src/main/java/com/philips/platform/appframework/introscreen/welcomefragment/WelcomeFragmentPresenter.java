package com.philips.platform.appframework.introscreen.welcomefragment;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivityPresenter;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.stateimpl.URStateListener;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WelcomeFragmentPresenter extends UIBasePresenter implements URStateListener {

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
    public void onClick(final int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
        welcomeFragmentView.showActionBar();
        String eventState = getEventState(componentID);
        if (eventState == WELCOME_DONE) {
            sharedPreferenceUtility = new SharedPreferenceUtility(welcomeFragmentView.getFragmentActivity());
            sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
        }
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.WELCOME, eventState);
        baseState.setPresenter(this);
        if (baseState instanceof UserRegistrationState)
            ((UserRegistrationState) baseState).registerUIStateListener(this);

        fragmentLauncher = getFragmentLauncher();
        baseState.navigate(fragmentLauncher);
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
    }

    // TODO: Deepthi, revisit this switch
    private String getEventState(final int componentID) {
        switch (componentID) {
            case R.id.welcome_skip_button:
                return WELCOME_SKIP;
            case R.id.welcome_start_registration_button:
                return WELCOME_DONE;
            case HomeActivityPresenter.MENU_OPTION_HOME:
                return WELCOME_HOME;
        }
        return null;
    }

    @Override
    public void onLoad() {

    }

    // TODO: Deepthi, check for condition and event and then take decision, can we move to json, pls check.
    @Override
    public void onStateComplete(final BaseState baseState) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
        String eventState = getEventState(HomeActivityPresenter.MENU_OPTION_HOME);
        this.baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.WELCOME, eventState);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.setPresenter(this);
        welcomeFragmentView.finishActivityAffinity();
        this.baseState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
