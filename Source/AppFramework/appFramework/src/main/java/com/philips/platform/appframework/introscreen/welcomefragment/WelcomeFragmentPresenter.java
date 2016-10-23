package com.philips.platform.appframework.introscreen.welcomefragment;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.homescreen.HomeActivityPresenter;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
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
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;
    private WelcomeFragmentView welcomeFragmentView;
    private FlowManager flowManager;

    public WelcomeFragmentPresenter(WelcomeFragmentView welcomeFragmentView) {
        super(welcomeFragmentView);
        this.welcomeFragmentView = welcomeFragmentView;
        flowManager = FlowManager.getInstance(welcomeFragmentView.getFragmentActivity());
    }

    @Override
    public void onClick(final int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
        welcomeFragmentView.showActionBar();
        String eventId = getEventID(componentID);
        uiState = flowManager.getState(eventId);
        uiState.setPresenter(this);
        fragmentLauncher = getFragmentLauncher();
        uiState.navigate(fragmentLauncher);
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
    }

    protected UIState getUiState(final int componentID) {
        switch (componentID) {
            case R.id.welcome_skip_button:
                uiState = new UserRegistrationState();
                uiState.setPresenter(this);
                ((UserRegistrationState) uiState).registerUIStateListener(this);
                break;
            case R.id.welcome_start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(welcomeFragmentView.getFragmentActivity());
                sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
                uiState = new UserRegistrationState();
                uiState.setPresenter(this);
                ((UserRegistrationState) uiState).registerUIStateListener(this);
                break;
            case HomeActivityPresenter.MENU_OPTION_HOME:
                uiState = new HomeActivityState();
        }
        return uiState;
    }

    private String getEventID(final int componentID) {
        switch (componentID) {
            case R.id.welcome_skip_button:
                return "welcome_skip";
            case R.id.welcome_start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(welcomeFragmentView.getFragmentActivity());
                sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
                return "welcome_done";
            case HomeActivityPresenter.MENU_OPTION_HOME:
                return "welcome_home";
        }
        return null;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onStateComplete(final UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
//        this.uiState = getUiState(HomeActivityPresenter.MENU_OPTION_HOME);
        String eventId = getEventID(HomeActivityPresenter.MENU_OPTION_HOME);
        this.uiState = flowManager.getState(eventId);
        fragmentLauncher = getFragmentLauncher();
        this.uiState.setPresenter(this);
        welcomeFragmentView.finishActivityAffinity();
//        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
        uiState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
