/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.settingscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.base.UIStateData;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
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

public class SettingsFragmentPresenterTest extends TestCase {

    private SettingsFragmentPresenter settingsFragmentPresenter;
    private SettingsView settingsViewMock;
    private FragmentActivity fragmentActivityMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        settingsViewMock = mock(SettingsView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(settingsViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock);
    }

    public void testLogOut() throws Exception {
        HomeFragmentState homeFragmentStateMock = mock(HomeFragmentState.class);
        final UIStateData uiStateMock = mock(UIStateData.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);

        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(AppStates.HAMBURGER_HOME);
            }

            @NonNull
            @Override
            protected UIStateData setStateData(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };

        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(AppStates.SETTINGS,"logout")).thenReturn(homeFragmentStateMock);
        settingsFragmentPresenter.onEvent(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);
        verify(homeFragmentStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testLogIn() throws Exception {
        final UserRegistrationSettingsState settingsURStateMock = mock(UserRegistrationSettingsState.class);
        final UIStateData uiStateMock = mock(UIStateData.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(AppStates.HAMBURGER_HOME);
            }

            @NonNull
            @Override
            protected UIStateData setStateData(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        JUnitFlowManager uiFlowManagerMock = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        when(uiFlowManagerMock.getNextState(AppStates.SETTINGS,"login")).thenReturn(settingsURStateMock);
        settingsFragmentPresenter.onEvent(1000004);
        verify(settingsURStateMock).navigate(fragmentLauncherMock);
    }

    /*public void testGetUIState() {
        assertTrue(settingsFragmentPresenter.setStateData(Constants.LOGOUT_BUTTON_CLICK_CONSTANT) instanceof HomeFragmentState);
        assertTrue(settingsFragmentPresenter.setStateData(999) instanceof UserRegistrationState);
        assertTrue(settingsFragmentPresenter.setStateData(998) instanceof HamburgerActivityState);
    }*/
}
