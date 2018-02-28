/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    private MyAccountState myAccountState;


    @Mock
    UIStateData uiStateData;

    @Mock
    private Context mockContext;
    private static final String LANGUAGE_TAG = "en-US";

    private List<ConsentConfiguration> configurations = new ArrayList<>();
    @Mock
    private ConsentHandlerInterface handler1;
    @Mock
    private ConsentHandlerInterface handler2;
    @Before
    public void setUp() {
        myAccountState = new MyAccountStateMock(myaInterface);

        myAccountState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(hamburgerActivity);

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

//    @Test
//    public void init_testApplicationAndPropositionName() {
//        ArgumentCaptor<CswDependencies> cswDependencies = ArgumentCaptor.forClass(CswDependencies.class);
//        myAccountState.setUiStateData(uiStateData);
//        myAccountState.navigate(fragmentLauncher);
//        // TODO: Deepthi, OBE to take care of this verification
//        //verify(myaInterface).init(cswDependencies.capture(), any(CswSettings.class));
//        assertEquals("OneBackend", cswDependencies.getValue().getApplicationName());
//        assertEquals("OneBackendProp", cswDependencies.getValue().getPropositionName());
//    }

    @Test
    public void shouldCreateNonNullListOfConsentDefinitions() throws Exception {
        assertNotNull(givenListOfConsentDefinitions());
    }

    @Test
    public void shouldAddOneSampleConsentDefinition() throws Exception {
        final List<ConsentDefinition> definitions = givenListOfConsentDefinitions();
        assertEquals(5, definitions.size());
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

    private List<ConsentDefinition> givenListOfConsentDefinitions() {
        return myAccountState.createCatkDefinitions(mockContext);
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


    @Test(expected = CatkInputs.InvalidInputException.class)
    public void itShouldThrowExceptionWhenSettingConfigurationsWithDuplicateTypes_in_single_configuration() {
        givenConfigurationsWithTypes(handler1, "moment", "moment");
        whenSettingConfiguration();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void itShouldThrowExceptionWhenSettingConfigurationsWithDuplicateTypes_in_multiple_configurations() {
        givenConfigurationsWithTypes(handler1, "moment", "consent");
        givenConfigurationsWithTypes(handler2, "moment", "coaching");
        whenSettingConfiguration();
    }

    @Test
    public void itShouldNotThrowExceptionWhenSettingConfigurationWithUniquesDuplicateTypes() {
        givenConfigurationsWithTypes(handler1, "moment", "consent");
        givenConfigurationsWithTypes(handler2, "coaching", "marketing");
        whenSettingConfiguration();
    }

    @Test
    public void itShouldNotThrowExceptionWhenSettingConfigurationWithoutTypes() {
        givenConfigurationsWithTypes(handler1);
        givenConfigurationsWithTypes(handler2);
        whenSettingConfiguration();
    }

    private void givenConfigurationsWithTypes(ConsentHandlerInterface handler, String... types) {
        List<ConsentDefinition> definitions = new ArrayList<>();
        for (String type : types) {
            definitions.add(createDefinitionsWithType(type));
        }
        configurations.add(new ConsentConfiguration(definitions, handler));
    }

    private void whenSettingConfiguration() {
        myAccountState.setConfigurations(configurations);
    }

    private ConsentDefinition createDefinitionsWithType(String type) {
        return new ConsentDefinition("text:" + type, "help:" + type, Collections.singletonList(type), 0, Locale.US);
    }
}