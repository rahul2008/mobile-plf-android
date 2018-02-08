/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.chi.datamodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ConsentTest {
    private static final String AMERICAN_LOCALE = "en-US";

    @Test
    public void isAccepted_trueIfHigherVersionBackendConsentIsActive() {
        givenActiveBackendConsentOfVersion(1);
        givenConsentDefinitionOfVersion(0);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsActive();
    }

    @Test
    public void isAccepted_falseWhenConsentIsRejected() {
        givenInactiveBackendConsentOfVersion(1);
        givenConsentDefinitionOfVersion(1);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsInactive();
    }

    @Test
    public void isAccepted_falseWhenThereIsNoConsent() {
        givenConsentDefinitionOfVersion(1);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsInactive();
    }

    @Test
    public void isAccepted_falseWhenConsentGivenButOldVersion() {
        givenActiveBackendConsentOfVersion(0);
        givenConsentDefinitionOfVersion(1);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsInactive();
    }

    @Test
    public void isAccepted_trueWhenConsentGivenAndSameVersion() {
        givenActiveBackendConsentOfVersion(1);
        givenConsentDefinitionOfVersion(1);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsActive();
    }

    @Test
    public void isAccepted_trueWhenMultipleConsentsAreGivenWithSameState() {
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenConsentDefinitionOfVersion(0);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsActive();
    }

    @Test
    public void isAccepted_falseWhenMultipleConsentsAreGivenWithDifferentState() {
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenInactiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenActiveBackendConsentOfVersion(0);
        givenConsentDefinitionOfVersion(0);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsInactive();
    }

    @Test
    public void isChangeable_falseWhenCurrentVersionHigherThanDefinition() {
        givenActiveBackendConsentOfVersion(1);
        givenConsentDefinitionOfVersion(0);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsNotChangeable();
    }

    @Test
    public void isChangeable_trueWhenThereIsNoConsent() {
        givenConsentDefinitionOfVersion(0);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsChangeable();
    }

    @Test
    public void isChangeable_trueWhenCurrentVersionLowerThanDefinition() {
        givenActiveBackendConsentOfVersion(0);
        givenConsentDefinitionOfVersion(1);
        whenRequiredConsentIsCreated();
        thenRequiredConsentIsChangeable();
    }

    private void givenInactiveBackendConsentOfVersion(int version) {
        backendConsent.add(new BackendConsent(AMERICAN_LOCALE, ConsentStatus.inactive, TYPE, version));
    }

    private void givenConsentDefinitionOfVersion(int version) {
        consentDefinition = new ConsentDefinition("someText1", "someHelpText1", Collections.singletonList(TYPE), version);
    }

    private void givenActiveBackendConsentOfVersion(int version) {
        backendConsent.add(new BackendConsent(AMERICAN_LOCALE, ConsentStatus.active, TYPE, version));
    }

    private void whenRequiredConsentIsCreated() {
        requiredConsent = new Consent(backendConsent, consentDefinition);
    }

    private void thenRequiredConsentIsActive() {
        assertTrue(requiredConsent.isAccepted());
    }

    private void thenRequiredConsentIsInactive() {
        assertFalse(requiredConsent.isAccepted());
    }

    private void thenRequiredConsentIsChangeable() {
        assertTrue(requiredConsent.isChangeable());
    }

    private void thenRequiredConsentIsNotChangeable() {
        assertFalse(requiredConsent.isChangeable());
    }

    private Consent requiredConsent;
    private ConsentDefinition consentDefinition;
    private List<BackendConsent> backendConsent = new ArrayList<>();
    private final String TYPE = "type1";

}