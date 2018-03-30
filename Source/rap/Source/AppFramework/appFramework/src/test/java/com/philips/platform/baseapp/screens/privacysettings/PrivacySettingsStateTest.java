package com.philips.platform.baseapp.screens.privacysettings;


import android.content.Context;

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

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class PrivacySettingsStateTest {
    private Context context;
    private List<ConsentDefinition> consentDefinitionList;

    @Mock
    private Context mockContext;

    private PrivacySettingsState privacySettingsState;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        privacySettingsState = new PrivacySettingsState();
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