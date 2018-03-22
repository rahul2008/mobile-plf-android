/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.telehealthservices;

import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeleHealthServicesStateTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private THSMicroAppInterfaceImpl thsMicroAppInterface;

    @Mock
    private FragmentLauncher fragmentLauncher;

    @Mock
    private HamburgerActivity hamburgerActivity;

    @Mock
    private AppFrameworkApplication application;

    @Mock
    private AppInfraInterface appInfraInterface;

    private TeleHealthServicesState teleHealthServicesState;

    @Mock
    UIStateData uiStateData;

    @Before
    public void setUp() {
        teleHealthServicesState = new TeleHealthServiceStateMock();
        teleHealthServicesState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        teleHealthServicesState.init(application);
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        when(hamburgerActivity.getApplicationContext()).thenReturn(application);
        when(application.getAppInfra()).thenReturn(appInfraInterface);
        when(application.getApplicationContext()).thenReturn(application);
    }

    @Test
    public void testLaunchTelehealthServicesState() {
        teleHealthServicesState.setUiStateData(uiStateData);
        teleHealthServicesState.navigate(fragmentLauncher);
        verify(thsMicroAppInterface).init(any(UappDependencies.class), any(UappSettings.class));
        verify(thsMicroAppInterface).launch(any(UiLauncher.class), any(UappLaunchInput.class));
    }

    @Test
    public void getMicroAppInterfaceTest() {
        TeleHealthServicesState teleHealthServicesState = new TeleHealthServicesState();
        assertNotNull(teleHealthServicesState.getMicroAppInterface());
    }

    @After
    public void tearDown() {
        thsMicroAppInterface = null;
        fragmentLauncher = null;
        hamburgerActivity = null;
        application = null;
        appInfraInterface = null;
        teleHealthServicesState = null;
        uiStateData = null;
    }

    class TeleHealthServiceStateMock extends TeleHealthServicesState {

        @Override
        protected THSMicroAppInterfaceImpl getMicroAppInterface() {
            return thsMicroAppInterface;
        }
    }
}