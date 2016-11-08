
package com.philips.platform.appframework.introscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.SplashState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.Before;

import static org.mockito.Mockito.atLeastOnce;
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
        final HamburgerActivityState hamburgerStateMock = mock(HamburgerActivityState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
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
        when(hamburgerStateMock.getStateID()).thenReturn(BaseAppState.WELCOME);
        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(BaseAppState.WELCOME,"welcome_home")).thenReturn(hamburgerStateMock);
        launchActivityPresenter.onClick(Constants.BACK_BUTTON_CLICK_CONSTANT);
        verify(hamburgerStateMock, atLeastOnce()).setPresenter(launchActivityPresenter);
        verify(hamburgerStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final HamburgerActivityState hamburgerStateMock = mock(HamburgerActivityState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
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

        final BaseState baseStateThisMock = mock(BaseState.class);
        when(uiFlowManagerMock.getNextState(BaseAppState.WELCOME,"welcome_home")).thenReturn(hamburgerStateMock);
        launchActivityPresenter.onStateComplete(baseStateThisMock);
        verify(welcomeViewMock).finishActivityAffinity();
        verify(hamburgerStateMock).setPresenter(launchActivityPresenter);
        verify(hamburgerStateMock).navigate(fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertEquals("welcome_home",launchActivityPresenter.getEventState(Constants.BACK_BUTTON_CLICK_CONSTANT));
    }

    public void testOnLoad() {
        final UIStateData uiStateData = mock(UIStateData.class);
        final SplashState splashStateMock = mock(SplashState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(welcomeViewMock) {
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

            @NonNull
            @Override
            protected UIStateData getUiStateData() {
                return uiStateData;
            }
        };
        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(null,null)).thenReturn(splashStateMock);
        launchActivityPresenter.onLoad();
        verify(welcomeViewMock).hideActionBar();
        verify(splashStateMock).setPresenter(launchActivityPresenter);
        verify(splashStateMock, atLeastOnce()).setUiStateData(uiStateData);
        verify(splashStateMock).navigate(fragmentLauncherMock);
    }
}
