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
        backendConsent = new Consent(Locale.US, ConsentStatus.inactive, TYPE, version);
    }

    private void givenConsentDefinitionOfVersion(int version) {
        consentDefinition = new ConsentDefinition("someText1", "someHelpText1", Collections.singletonList(TYPE), version, Locale.US);
    }

    private void givenActiveBackendConsentOfVersion(int version) {
        backendConsent = new Consent(Locale.US, ConsentStatus.active, TYPE, version);
    }

    private void givenInaciveBackendConsentOfVersion(int version) {
        backendConsent = new Consent(Locale.US, ConsentStatus.active, TYPE, version);
    }

    private void whenRequiredConsentIsCreated() {
        requiredConsent = new RequiredConsent(backendConsent, consentDefinition);
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

    private RequiredConsent requiredConsent;
    private ConsentDefinition consentDefinition;
    private Consent backendConsent;
    private final String TYPE = "type1";

}