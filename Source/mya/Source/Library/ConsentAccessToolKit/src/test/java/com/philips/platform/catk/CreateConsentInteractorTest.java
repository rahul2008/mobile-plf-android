/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentDefinitionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Locale;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

public class CreateConsentInteractorTest {

    private CreateConsentInteractor subject;
    @Mock
    private ConsentAccessToolKit mockCatk;
    private ConsentDefinition givenConsentDefinition;
    @Captor
    private ArgumentCaptor<BackendConsent> captorConsent;
    @Mock
    private CreateConsentInteractor.Callback mockCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itShouldCallCreateConsentOnTheCatk() throws Exception {
        givenCreateConsentInteractor();
        givenConsentDefinition(Locale.getDefault());
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test(expected = ConsentDefinitionException.class)
    public void itShouldThrowConsentDefinitionExceptionWhenUsingDefinitionWithLocaleThatIsMissingCountry() throws Exception {
        givenCreateConsentInteractor();
        givenConsentDefinition(new Locale("nl", ""));
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test(expected = ConsentDefinitionException.class)
    public void itShouldThrowConsentDefinitionExceptionWhenUsingDefinitionWithLocaleThatIsMissingLanguage() throws Exception {
        givenCreateConsentInteractor();
        givenConsentDefinition(new Locale("", "NL"));
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    private void givenCreateConsentInteractor() {
        subject = new CreateConsentInteractor(mockCatk);
    }

    private void givenConsentDefinition(Locale locale) {
        givenConsentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("moment"), 0, locale);
    }

    private void whenCallingCreateConsentInGivenState(boolean checked) {
        subject.createConsentStatus(givenConsentDefinition, mockCallback, checked);
    }

    private void thenCreateConsentIsCalledOnTheCatk() {
        verify(mockCatk).createConsent(Collections.singletonList(captorConsent.capture()), isA(CreateConsentListener.class));
    }

}