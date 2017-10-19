/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.myaccount;

import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.MyaDependencies;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyAccountStateTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private MyaInterface thsMicroAppInterface;

    @Mock
    private FragmentLauncher fragmentLauncher;

    @Mock
    private HamburgerActivity hamburgerActivity;

    @Mock
    private AppFrameworkApplication application;

    @Mock
    private AppInfraInterface appInfraInterface;

    private MyAccountState myAccountState;

    @Mock
    UIStateData uiStateData;

    @Before
    public void setUp() {
        myAccountState = new MyAccountStateMock();
        myAccountState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        myAccountState.init(application);
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        when(hamburgerActivity.getApplicationContext()).thenReturn(application);
        when(application.getAppInfra()).thenReturn(appInfraInterface);
    }

    @Test
    public void testLaunchTelehealthServicesState() {
        myAccountState.setUiStateData(uiStateData);
        myAccountState.navigate(fragmentLauncher);
        verify(thsMicroAppInterface).init(any(MyaDependencies.class), any(MyaSettings.class));
        verify(thsMicroAppInterface).launch(any(FragmentLauncher.class), any(MyaLaunchInput.class));
    }

    @After
    public void tearDown() {
        thsMicroAppInterface = null;
        fragmentLauncher = null;
        hamburgerActivity = null;
        application = null;
        appInfraInterface = null;
        myAccountState = null;
        uiStateData = null;
    }

    class MyAccountStateMock extends MyAccountState {

    }
}