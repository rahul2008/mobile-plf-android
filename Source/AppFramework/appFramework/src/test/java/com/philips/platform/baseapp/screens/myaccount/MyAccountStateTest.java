/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.myaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyAccountStateTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private MyaInterface myaInterface;

    @Mock
    private FragmentLauncher fragmentLauncher;

    @Mock
    private HamburgerActivity hamburgerActivity;

    @Mock
    private AppFrameworkApplication application;

    @Mock
    private FragmentManager fragmentManager;

    @Mock
    private AppInfraInterface appInfraInterface;

    @Mock
    private FragmentTransaction fragmentTransaction;

    private MyAccountState myAccountState;

    @Mock
    UIStateData uiStateData;

    @Before
    public void setUp() {
        myAccountState = new MyAccountStateMock(myaInterface);
        myAccountState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        myAccountState.init(application);
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        when(hamburgerActivity.getApplicationContext()).thenReturn(application);
        when(hamburgerActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(fragmentTransaction.replace(any(Integer.class), any(Fragment.class), any(String.class))).thenReturn(fragmentTransaction);
        when(application.getAppInfra()).thenReturn(appInfraInterface);
    }

    @Test
    public void testLaunchMyAccountState() {
        myAccountState.setUiStateData(uiStateData);
        myAccountState.navigate(fragmentLauncher);
        verify(myaInterface).init(any(MyaDependencies.class), any(MyaSettings.class));
        verify(myaInterface).launch(any(FragmentLauncher.class), any(MyaLaunchInput.class));
    }

    @Test
    public void init_testApplicationAndPropositionName() {
        ArgumentCaptor<MyaDependencies> myaDependencies = ArgumentCaptor.forClass(MyaDependencies.class);
        myAccountState.setUiStateData(uiStateData);
        myAccountState.navigate(fragmentLauncher);
        verify(myaInterface).init(myaDependencies.capture(), any(MyaSettings.class));
        assertEquals("OneBackend", myaDependencies.getValue().getApplicationName());
        assertEquals("OneBackendProp", myaDependencies.getValue().getPropositionName());
    }

    @After
    public void tearDown() {
        myaInterface = null;
        fragmentLauncher = null;
        hamburgerActivity = null;
        application = null;
        appInfraInterface = null;
        myAccountState = null;
        uiStateData = null;
    }

    class MyAccountStateMock extends MyAccountState {

        private MyaInterface myaInterface;

        public MyAccountStateMock(MyaInterface myaInterface) {
            this.myaInterface = myaInterface;
        }

        @Override
        public MyaInterface getInterface() {
            return myaInterface;
        }
    }
}