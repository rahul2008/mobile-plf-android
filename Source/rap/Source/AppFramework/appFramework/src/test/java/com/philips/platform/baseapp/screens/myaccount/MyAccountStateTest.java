/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class MyAccountStateTest {
    private static final String PRIVACY_URL = "http://google.com";

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

    @Mock
    private Context mockContext;

    @Mock
    private UserRegistrationState userRegistrationStateMock;

    @Mock
    private UserDataInterface userDataInterfaceMock;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    AppTaggingInterface appTaggingInterfaceMock;

    @Mock
    private ABTestClientInterface abTestClientInterface;

    private static final String LANGUAGE_TAG = "en-US";
    private Context context;

    @Mock
    private Resources resources;

    @Mock
    private ConsentManagerInterface consentManagerInterfaceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        myAccountState = new MyAccountStateMock(myaInterface);
        context = RuntimeEnvironment.application;
        myAccountState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        when(application.getUserRegistrationState()).thenReturn(userRegistrationStateMock);
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);
        when(hamburgerActivity.getApplicationContext()).thenReturn(application);
        when(hamburgerActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(fragmentTransaction.replace(any(Integer.class), any(Fragment.class), any(String.class))).thenReturn(fragmentTransaction);
        when(application.getAppInfra()).thenReturn(appInfraInterface);
        when(application.getUserRegistrationState()).thenReturn(userRegistrationStateMock);
        when(userRegistrationStateMock.getUserDataInterface()).thenReturn(userDataInterfaceMock);
        when(mockContext.getResources()).thenReturn(resources);
        when(mockContext.getApplicationContext()).thenReturn(application);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterfaceMock);
        when(appInfraInterface.getConsentManager()).thenReturn(consentManagerInterfaceMock);
        when(resources.getString(anyInt())).thenReturn("ABC");
        when(appInfraInterface.getAbTesting()).thenReturn(abTestClientInterface);
        when(abTestClientInterface.getCacheStatus()).thenReturn(ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
    }

    @Test
    public void testLaunchMyAccountState() {
        myAccountState.setUiStateData(uiStateData);
        myAccountState.navigate(fragmentLauncher);
        verify(myaInterface).init(any(MyaDependencies.class), any(MyaSettings.class));
        verify(myaInterface).launch(any(FragmentLauncher.class), any(MyaLaunchInput.class));
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

        @Override
        protected AppFrameworkApplication getApplicationContext() {
            return application;
        }
    }

}