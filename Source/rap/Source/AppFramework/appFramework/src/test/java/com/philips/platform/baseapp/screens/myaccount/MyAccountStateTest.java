/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
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

    @Mock
    private ConsentManagerInterface consentManagerInterface;

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

    private static final String LANGUAGE_TAG = "en-US";
    private Context context;

    @Mock
    private ConsentHandlerInterface handler1;
    @Mock
    private ConsentHandlerInterface handler2;

    @Mock
    private Resources resources;

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
        when(resources.getString(anyInt())).thenReturn("ABC");
    }

    @Test
    public void testLaunchMyAccountState() {
        myAccountState.setUiStateData(uiStateData);
        myAccountState.navigate(fragmentLauncher);
        verify(myaInterface).init(any(MyaDependencies.class), any(MyaSettings.class));
        verify(myaInterface).launch(any(FragmentLauncher.class), any(MyaLaunchInput.class));
    }

    @Test
    public void shouldCreateNonNullListOfConsentDefinitions() throws Exception {
        assertNotNull(givenListOfConsentDefinitions());
    }

    @Test
    public void shouldAddOneSampleConsentDefinition() throws Exception {
        final List<ConsentDefinition> definitions = givenListOfConsentDefinitions();
        assertEquals(7, definitions.size());
    }

    @After
    public void tearDown() {
        myaInterface = null;
        fragmentLauncher = null;
        hamburgerActivity = null;
        application = null;
        appInfraInterface = null;
        consentManagerInterface = null;
        myAccountState = null;
        uiStateData = null;
        appFrameworkApplication = null;
    }

    private List<ConsentDefinition> givenListOfConsentDefinitions() {
        return myAccountState.getConsentDefinitions(mockContext);
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