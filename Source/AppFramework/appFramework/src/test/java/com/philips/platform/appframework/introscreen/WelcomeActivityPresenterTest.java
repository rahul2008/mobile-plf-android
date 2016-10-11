package com.philips.platform.appframework.introscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
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
public class WelcomeActivityPresenterTest extends TestCase {

    private WelcomeActivityPresenter welcomeActivityPresenter;
    private WelcomeView welcomeViewMock;
    private FragmentActivity fragmentActivityMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        welcomeViewMock = mock(WelcomeView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(welcomeViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeActivityPresenter = new WelcomeActivityPresenter(welcomeViewMock);
    }

    public void testOnClick() throws Exception {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeActivityPresenter = new WelcomeActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState getUiState(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        when(uiStateMock.getStateID()).thenReturn(UIState.UI_USER_REGISTRATION_STATE);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(uiFlowManagerMock.getCurrentState()).thenReturn(uiStateMock);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeActivityPresenter.onClick(Constants.BACK_BUTTON_CLICK_CONSTANT);
        verify(welcomeViewMock).finishActivityAffinity();
        verify(uiStateMock).setPresenter(welcomeActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeActivityPresenter = new WelcomeActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState getUiState(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        when(uiStateMock.getStateID()).thenReturn(UIState.UI_USER_REGISTRATION_STATE);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(uiFlowManagerMock.getCurrentState()).thenReturn(uiStateMock);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final UIState uiStateThisMock = mock(UIState.class);
        welcomeActivityPresenter.onStateComplete(uiStateThisMock);
        verify(welcomeViewMock).finishActivityAffinity();
        verify(uiStateMock).setPresenter(welcomeActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertTrue(welcomeActivityPresenter.getUiState(Constants.BACK_BUTTON_CLICK_CONSTANT) instanceof HomeActivityState);
    }

    public void testOnLoad() {
        final UserRegistrationState uiStateMock = mock(UserRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final SharedPreferenceUtility sharedPreferenceUtilityMock = mock(SharedPreferenceUtility.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeActivityPresenter = new WelcomeActivityPresenter(welcomeViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState getUiState(final int componentID) {
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

        welcomeActivityPresenter.onLoad();
        verify(welcomeViewMock).hideActionBar();
        verify(welcomeViewMock).loadWelcomeFragment();

        when(sharedPreferenceUtilityMock.getPreferenceBoolean(Constants.DONE_PRESSED)).thenReturn(true);
        when(uiStateMock.getStateID()).thenReturn(UIState.UI_USER_REGISTRATION_STATE);
        welcomeActivityPresenter.onLoad();
        verify(welcomeViewMock).showActionBar();
        verify(uiStateMock).setPresenter(welcomeActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }
}