package com.philips.platform.appframework.settingscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
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
        final UserRegistrationState uiStateMock = mock(UserRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState setStateData(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        settingsFragmentPresenter.onClick(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);
        verify(uiStateMock).setPresenter(settingsFragmentPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testOnLoad() throws Exception {
        final UserRegistrationState uiStateMock = mock(UserRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState setStateData(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        settingsFragmentPresenter.onLoad();
        verify(uiStateMock).setPresenter(settingsFragmentPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        settingsFragmentPresenter = new SettingsFragmentPresenter(settingsViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

            @NonNull
            @Override
            protected UIState setStateData(final int componentID) {
                return uiStateMock;
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final UIState uiStateThisMock = mock(UIState.class);
        settingsFragmentPresenter.onStateComplete(uiStateThisMock);
        verify(uiStateMock).setPresenter(settingsFragmentPresenter);
        verify(settingsViewMock).finishActivityAffinity();
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testGetUIState() {
        assertTrue(settingsFragmentPresenter.setStateData(Constants.LOGOUT_BUTTON_CLICK_CONSTANT) instanceof HomeFragmentState);
        assertTrue(settingsFragmentPresenter.setStateData(999) instanceof UserRegistrationState);
        assertTrue(settingsFragmentPresenter.setStateData(998) instanceof HomeActivityState);
    }
}