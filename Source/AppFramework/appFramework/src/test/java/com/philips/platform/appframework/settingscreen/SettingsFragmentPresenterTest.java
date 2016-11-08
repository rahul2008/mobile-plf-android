
package com.philips.platform.appframework.settingscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
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

    public void testOnClick() throws Exception {
        HomeFragmentState homeFragmentStateMock = mock(HomeFragmentState.class);
        final UIStateData uiStateMock = mock(UIStateData.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);

        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
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
        when(uiFlowManagerMock.getNextState(BaseAppState.SETTINGS,"logout")).thenReturn(homeFragmentStateMock);
        settingsFragmentPresenter.onClick(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);
        verify(homeFragmentStateMock, atLeastOnce()).setPresenter(settingsFragmentPresenter);
        verify(homeFragmentStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final HamburgerActivityState hamburgerStateMock = mock(HamburgerActivityState.class);
        final UIStateData uiStateMock = mock(UIStateData.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
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
        when(uiFlowManagerMock.getNextState(BaseAppState.SETTINGS,"settings_registration")).thenReturn(hamburgerStateMock);
        settingsFragmentPresenter.onStateComplete(hamburgerStateMock);
        verify(settingsViewMock).finishActivityAffinity();
        verify(hamburgerStateMock).setPresenter(settingsFragmentPresenter);
        verify(hamburgerStateMock).navigate(fragmentLauncherMock);
    }

    /*public void testGetUIState() {
        assertTrue(settingsFragmentPresenter.setStateData(Constants.LOGOUT_BUTTON_CLICK_CONSTANT) instanceof HomeFragmentState);
        assertTrue(settingsFragmentPresenter.setStateData(999) instanceof UserRegistrationState);
        assertTrue(settingsFragmentPresenter.setStateData(998) instanceof HamburgerActivityState);
    }*/
}
