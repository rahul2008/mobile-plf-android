package com.philips.platform.appframework.introscreen.welcomefragment;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.introscreen.WelcomeView;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
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
    private WelcomeView welcomeViewMock;
    private FragmentActivity fragmentActivityMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        welcomeFragmentViewMock = mock(WelcomeFragmentView.class);
        welcomeViewMock = mock(WelcomeView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(welcomeFragmentViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock);
    }

    public void testOnClick() throws Exception {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock) {
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
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeFragmentPresenter.onClick(R.id.welcome_skip_button);
        verify(welcomeFragmentViewMock).showActionBar();
        verify(uiStateMock).setPresenter(welcomeFragmentPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testOnStateComplete() throws Exception {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        welcomeFragmentPresenter = new WelcomeFragmentPresenter(welcomeFragmentViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_WELCOME_STATE);
            }

            @Override
            protected UIState getUiState(final int componentID) {
                return uiStateMock;
            }

            @NonNull
            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }
        };
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        UIFlowManager uiFlowManagerMock = mock(UIFlowManager.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);

        final UIState uiStateMockThis = mock(UIState.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeFragmentPresenter.onClick(6);
        welcomeFragmentPresenter.onStateComplete(uiStateMockThis);
        verify(uiStateMock, atLeastOnce()).setPresenter(welcomeFragmentPresenter);
        verify(uiFlowManagerMock, atLeastOnce()).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertEquals(true, welcomeFragmentPresenter.getUiState(R.id.welcome_skip_button) instanceof UserRegistrationState);
    }
}