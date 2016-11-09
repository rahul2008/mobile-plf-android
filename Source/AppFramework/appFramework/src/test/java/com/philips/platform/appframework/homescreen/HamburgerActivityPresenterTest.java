/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.JUnitFlowManager;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import junit.framework.TestCase;
import java.util.ArrayList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerActivityPresenterTest extends TestCase {

    FragmentView fragmentViewMock;
    private HamburgerActivityPresenter hamburgerActivityPresenter;
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
        hamburgerActivityPresenter = new HamburgerActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
            }

            @NonNull
            @Override
            protected ArrayList<String> getCtnList() {
                ArrayList<String> ctnList = new ArrayList<>();
                ctnList.add("HX6064/33");
                return ctnList;
            }
        };
    }

    public void testGetUiState() {
        final FragmentView fragmentViewMock = mock(FragmentView.class);
        final FragmentActivity fragmentActivityMock = mock(FragmentActivity.class);
        final Resources resourcesMock = mock(Resources.class);
        when(fragmentViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(fragmentActivityMock.getResources()).thenReturn(resourcesMock);
        assertEquals(true, hamburgerActivityPresenter.setStateData(0) instanceof UIStateData);
        assertEquals(true, hamburgerActivityPresenter.setStateData(1) instanceof UIStateData);
        assertEquals(true, hamburgerActivityPresenter.setStateData(2) instanceof IAPState.InAppStateData);
        assertEquals(true, hamburgerActivityPresenter.setStateData(3) instanceof SupportFragmentState.ConsumerCareData);
        assertEquals(true, hamburgerActivityPresenter.setStateData(4) instanceof UIStateData);
        assertEquals(true, hamburgerActivityPresenter.setStateData(-1) instanceof UIStateData);
        assertFalse(hamburgerActivityPresenter.setStateData(5) instanceof SupportFragmentState.ConsumerCareData);
    }

    public void testOnClick() {
        final UIStateData uiStateData = mock(UIStateData.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final HomeFragmentState homeFragmentStateMock = mock(HomeFragmentState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        hamburgerActivityPresenter = new HamburgerActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
            }

            @NonNull
            @Override
            protected UIStateData setStateData(final int componentID) {
                return uiStateData;
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

        JUnitFlowManager uiFlowManager = mock(JUnitFlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManager);
        when(uiFlowManager.getNextState(HamburgerAppState.HAMBURGER_HOME, "home_fragment")).thenReturn(homeFragmentStateMock);
        hamburgerActivityPresenter.onClick(0);
        verify(homeFragmentStateMock, atLeastOnce()).setPresenter(hamburgerActivityPresenter);
        verify(homeFragmentStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testOnStateComplete() {
        final ProductRegistrationState.ProductRegistrationData prStateData = mock(ProductRegistrationState.ProductRegistrationData.class);
        final ProductRegistrationState productRegistrationStateMock = mock(ProductRegistrationState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        hamburgerActivityPresenter = new HamburgerActivityPresenter(fragmentViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(HamburgerAppState.HAMBURGER_HOME);
            }

            @Override
            protected UIStateData setStateData(final int componentID) {
                return prStateData;
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
        when(uiFlowManagerMock.getNextState(HamburgerAppState.SUPPORT, "pr")).thenReturn(productRegistrationStateMock);
        hamburgerActivityPresenter.onStateComplete(productRegistrationStateMock);
        verify(productRegistrationStateMock, atLeastOnce()).setPresenter(hamburgerActivityPresenter);
        verify(productRegistrationStateMock, atLeastOnce()).setUiStateData(prStateData);
        verify(productRegistrationStateMock).navigate(fragmentLauncherMock);
    }
}