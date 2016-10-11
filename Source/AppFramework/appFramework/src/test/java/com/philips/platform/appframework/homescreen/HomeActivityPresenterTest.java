package com.philips.platform.appframework.homescreen;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.TestCase;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HomeActivityPresenterTest extends TestCase {

    FragmentView fragmentViewMock;
    private HomeActivityPresenter homeActivityPresenter;
    private FragmentActivity fragmentActivityMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentViewMock = mock(FragmentView.class);
        Resources resourcesMock = mock(Resources.class);
        when(resourcesMock.getStringArray(R.array.productselection_ctnlist)).thenReturn(new String[]{"abcd"});
        fragmentActivityMock = mock(FragmentActivity.class);
        ActionBarListener actionBarListenerMock = mock(ActionBarListener.class);
        when(fragmentActivityMock.getResources()).thenReturn(resourcesMock);
        when(fragmentViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(fragmentViewMock.getContainerId()).thenReturn(12345);
        when(fragmentViewMock.getActionBarListener()).thenReturn(actionBarListenerMock);
        homeActivityPresenter = new HomeActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }
        };
    }

    public void testGetUiState() {
        assertEquals(true, homeActivityPresenter.getUiState(0) instanceof HomeFragmentState);
        assertEquals(true, homeActivityPresenter.getUiState(1) instanceof SettingsFragmentState);
        assertEquals(true, homeActivityPresenter.getUiState(2) instanceof IAPState);
        assertEquals(true, homeActivityPresenter.getUiState(3) instanceof SupportFragmentState);
        assertEquals(true, homeActivityPresenter.getUiState(4) instanceof AboutScreenState);
        assertEquals(true, homeActivityPresenter.getUiState(5) instanceof DebugTestFragmentState);
        assertEquals(true, homeActivityPresenter.getUiState(-1) instanceof HomeFragmentState);
        assertFalse(homeActivityPresenter.getUiState(5) instanceof AboutScreenState);
    }

    public void testOnClick() {
        final UIState uiStateMock = mock(UIState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        homeActivityPresenter = new HomeActivityPresenter(fragmentViewMock) {
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
        homeActivityPresenter.onClick(0);
        verify(uiStateMock).setPresenter(homeActivityPresenter);
        verify(uiFlowManagerMock).navigateToState(uiStateMock, fragmentLauncherMock);
    }

    public void testOnStateComplete() {
        final ProductRegistrationState uiStateMock = mock(ProductRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        homeActivityPresenter = new HomeActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final int stateID) {
                super.setState(UIState.UI_HOME_STATE);
            }

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

        final UIState uiStateMockThis = mock(UIState.class);
        when(appFrameworkApplicationMock.getFlowManager()).thenReturn(uiFlowManagerMock);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        homeActivityPresenter.onClick(6);
        homeActivityPresenter.onStateComplete(uiStateMockThis);
        verify(uiStateMock, atLeastOnce()).setPresenter(homeActivityPresenter);
        verify(uiFlowManagerMock, atLeastOnce()).navigateToState(uiStateMock, fragmentLauncherMock);
    }
}