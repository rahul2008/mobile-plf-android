package com.philips.platform.appframework.introscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LaunchActivityPresenterTest extends TestCase {

    private LaunchActivityPresenter launchActivityPresenter;
    private WelcomeView welcomeViewMock;
    private FragmentActivity fragmentActivityMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        welcomeViewMock = mock(WelcomeView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(welcomeViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock);
    }

    public void testOnClick() throws Exception {
        final BaseState baseStateMock = mock(BaseState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(BaseState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected BaseState getUiState(final int componentID) {
                return baseStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        when(baseStateMock.getStateID()).thenReturn(BaseState.UI_USER_REGISTRATION_STATE);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(uiFlowManagerMock.getCurrentState()).thenReturn(baseStateMock);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        launchActivityPresenter.onClick(Constants.BACK_BUTTON_CLICK_CONSTANT);
        verify(welcomeViewMock).finishActivityAffinity();
        verify(baseStateMock).setPresenter(launchActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(baseStateMock, fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final BaseState baseStateMock = mock(BaseState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(BaseState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected BaseState getUiState(final int componentID) {
                return baseStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        when(baseStateMock.getStateID()).thenReturn(BaseState.UI_USER_REGISTRATION_STATE);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(uiFlowManagerMock.getCurrentState()).thenReturn(baseStateMock);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final BaseState baseStateThisMock = mock(BaseState.class);
        launchActivityPresenter.onStateComplete(baseStateThisMock);
        verify(welcomeViewMock).finishActivityAffinity();
        verify(baseStateMock).setPresenter(launchActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(baseStateMock, fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertTrue(launchActivityPresenter.getUiState(Constants.BACK_BUTTON_CLICK_CONSTANT) instanceof HamburgerActivityState);
    }

    public void testOnLoad() {
        final UserRegistrationState uiStateMock = mock(UserRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final SharedPreferenceUtility sharedPreferenceUtilityMock = mock(SharedPreferenceUtility.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(BaseState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected BaseState getUiState(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }

            @NonNull
            @Override
            protected SharedPreferenceUtility getSharedPreferenceUtility() {
                return sharedPreferenceUtilityMock;
            }
        };
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(sharedPreferenceUtilityMock.getPreferenceBoolean(Constants.DONE_PRESSED)).thenReturn(false);
        when(uiFlowManagerMock.getCurrentState()).thenReturn(uiStateMock);
        when(uiStateMock.getStateID()).thenReturn(45689);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);

        launchActivityPresenter.onLoad();
        verify(welcomeViewMock).hideActionBar();
//        verify(welcomeViewMock).loadWelcomeFragment();

        when(sharedPreferenceUtilityMock.getPreferenceBoolean(Constants.DONE_PRESSED)).thenReturn(true);
        when(uiStateMock.getStateID()).thenReturn(BaseState.UI_USER_REGISTRATION_STATE);
        launchActivityPresenter.onLoad();
        verify(welcomeViewMock).showActionBar();
        verify(uiStateMock).setPresenter(launchActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }
}