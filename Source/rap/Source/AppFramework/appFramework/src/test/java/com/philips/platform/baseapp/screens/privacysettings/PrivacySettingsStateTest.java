package com.philips.platform.baseapp.screens.privacysettings;


import android.content.Context;
import android.content.res.Resources;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class PrivacySettingsStateTest {
    private Context context;
    private List<ConsentDefinition> consentDefinitionList;

    @Mock
    private Context mockContext;

    @Mock
    private AppInfraInterface appInfraInterface;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    private AppFrameworkApplication application;

    @Mock
    private ConsentManagerInterface consentManagerInterfaceMock;

    @Mock
    private Resources resources;

    private PrivacySettingsState privacySettingsState;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        privacySettingsState = new PrivacySettingsState();
        when(mockContext.getApplicationContext()).thenReturn(application);
        when(mockContext.getResources()).thenReturn(resources);
        when(application.getAppInfra()).thenReturn(appInfraInterface);
        when(appInfraInterface.getConsentManager()).thenReturn(consentManagerInterfaceMock);
        when(resources.getString(anyInt())).thenReturn("ABC");
    }
    @Test
    public void shouldCreateNonNullListOfConsentDefinitions() throws Exception {
        assertNotNull(givenListOfConsentDefinitions());
    }

    @Test
    public void shouldAddOneSampleConsentDefinition() throws Exception {
        final List<ConsentDefinition> definitions = givenListOfConsentDefinitions();
        assertEquals(8, definitions.size());
    }

    @Test
    public void shouldContainRevokeWarningConsentDefinition() throws Exception {
        consentDefinitionList = givenListOfConsentDefinitions();
        thenDefnitionsShouldContainRevokeWarning("moment");
        thenDefnitionsShouldContainRevokeWarning("coaching");
        thenDefnitionsShouldContainRevokeWarning("binary");
        thenDefnitionsShouldContainRevokeWarning("research");
        thenDefnitionsShouldContainRevokeWarning("analytics");
    }

    private List<ConsentDefinition> givenListOfConsentDefinitions() {
        return privacySettingsState.createConsentDefinitions(mockContext);
    }

    private void thenDefnitionsShouldContainRevokeWarning(String consentType) {
        for (ConsentDefinition consentDefinition : consentDefinitionList) {
            if (consentDefinition.getTypes().contains(consentType)) {
                assertTrue(consentDefinition.hasRevokeWarningText());
                return;
            }
        }
    }
}