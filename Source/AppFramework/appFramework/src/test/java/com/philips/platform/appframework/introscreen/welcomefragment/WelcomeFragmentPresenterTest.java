
package com.philips.platform.appframework.introscreen.welcomefragment;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class WelcomeFragmentPresenterTest extends TestCase {

    private WelcomeFragmentPresenter welcomeFragmentPresenter;
    private WelcomeFragmentView welcomeFragmentViewMock;
    private FragmentActivity fragmentActivityMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        welcomeFragmentViewMock = mock(WelcomeFragmentView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(welcomeFragmentViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock);
    }

    public void testOnClick() throws Exception {
        final UserRegistrationState userRegStateMock = mock(UserRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }

            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplicationMock;
            }
        };
        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(HamburgerAppState.WELCOME,"welcome_skip")).thenReturn(userRegStateMock);
        welcomeFragmentPresenter.onClick(R.id.welcome_skip_button);
        verify(welcomeFragmentViewMock).showActionBar();
        verify(userRegStateMock, atLeastOnce()).setPresenter(welcomeFragmentPresenter);
        verify(userRegStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final UserRegistrationState userRegStateMock = mock(UserRegistrationState.class);
        final HamburgerActivityState hamburgerActivityStateMock = mock(HamburgerActivityState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.WELCOME);
            }

            @NonNull
            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplicationMock;
            }
        };
        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(HamburgerAppState.WELCOME,"welcome_home")).thenReturn(hamburgerActivityStateMock);
        welcomeFragmentPresenter.onStateComplete(userRegStateMock);
        verify(hamburgerActivityStateMock, atLeastOnce()).setPresenter(welcomeFragmentPresenter);
        verify(hamburgerActivityStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertEquals("welcome_done", welcomeFragmentPresenter.getEventState(R.id.welcome_start_registration_button));
    }
}
