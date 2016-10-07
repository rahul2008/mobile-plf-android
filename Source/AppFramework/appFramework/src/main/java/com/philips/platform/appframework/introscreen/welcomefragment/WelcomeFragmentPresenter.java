package com.philips.platform.appframework.introscreen.welcomefragment;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WelcomeFragmentPresenter extends UIBasePresenter implements UIStateListener {

    private AppFrameworkApplication appFrameworkApplication;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;
    private WelcomeFragmentView welcomeFragmentView;

    public WelcomeFragmentPresenter(WelcomeFragmentView welcomeFragmentView) {
        super(welcomeFragmentView);
        this.welcomeFragmentView = welcomeFragmentView;
    }


    @Override
    public void onClick(final int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
        welcomeFragmentView.showActionBar();
        switch (componentID) {
            case R.id.welcome_skip_button:
                uiState = new UserRegistrationState();
                fragmentLauncher = new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
                uiState.setPresenter(this);
                ((UserRegistrationState) uiState).registerUIStateListener(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
                break;
            case R.id.welcome_start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(welcomeFragmentView.getFragmentActivity());
                sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
                uiState = new UserRegistrationState();
                fragmentLauncher = new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
                uiState.setPresenter(this);
                ((UserRegistrationState) uiState).registerUIStateListener(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
                break;
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onStateComplete(final UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeFragmentView.getFragmentActivity().getApplicationContext();
        this.uiState = new HomeActivityState();
        fragmentLauncher = new FragmentLauncher(welcomeFragmentView.getFragmentActivity(), welcomeFragmentView.getContainerId(), welcomeFragmentView.getActionBarListener());
        this.uiState.setPresenter(this);
        welcomeFragmentView.finishActivityAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }
}
