/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.model;

import org.junit.Test;

import java.util.Locale;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.junit.Assert.*;

public class RequiredConsentTest {

    @Test
    public void isAccepted_trueIfHigherVersionBackendConsentIsActive () {
        givenActiveBackendConsentOfVersion(1);
        givenConsentDefinitionOfVersion(0);
        thenRequiredConsentIsActive();
    }

    private void givenConsentDefinitionOfVersion(int version) {
        consentDefinition = new ConsentDefinition("someText1", "someHelpText1", Collections.singletonList(TYPE), version, Locale.US);
    }

    private void givenActiveBackendConsentOfVersion(int version) {
        backendConsent = new Consent(Locale.US, ConsentStatus.active, TYPE, version);
    }

    private void thenRequiredConsentIsActive() {
        requiredConsent = new RequiredConsent(backendConsent, consentDefinition);
        assertTrue(requiredConsent.isAccepted());
    }

    private RequiredConsent requiredConsent;
    private ConsentDefinition consentDefinition;
    private Consent backendConsent;
    private final String TYPE = "type1";

}