/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mock.LoggingInterfaceMock;
import com.philips.platform.catk.utils.CatkLogger;
import com.philips.platform.consenthandlerinterface.ConsentCallback;
import com.philips.platform.consenthandlerinterface.ConsentDefinitionException;
import com.philips.platform.consenthandlerinterface.ConsentError;
import com.philips.platform.consenthandlerinterface.CheckConsentsCallback;
import com.philips.platform.consenthandlerinterface.PostConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.BackendConsent;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {

    ConsentInteractor subject;

    @Mock
    private ConsentAccessToolKit mockContentAccessToolkit;
    @Mock
    private CheckConsentsCallback mockCheckConsentsCallback;
    @Captor
    private ArgumentCaptor<ConsentInteractor.GetConsentsResponseListener> captorConsentDetails;
    @Captor
    private ArgumentCaptor<List<Consent>> captorRequired;
    @Captor
    private ArgumentCaptor<String> captorString;
    @Mock
    private ConsentAccessToolKit mockCatk;
    private ConsentDefinition givenConsentDefinition;
    @Captor
    private ArgumentCaptor<BackendConsent> captorConsent;
    @Mock
    private PostConsentCallback mockPostConsentCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());
    }

    @Test
    public void fetchLatestConsents_itShouldGetConsentDetails() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenFetchLatestConsentsCalled();
        thenGetConsentDetailsIsCalled();
    }

    @Test
    public void checkConsents_itShouldGetConsentDetails() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenCheckConsentsCalled();
        thenGetConsentDetailsIsCalled();
    }

    @Test
    public void itShouldReportConsentRetrievedWhenEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenFetchLatestConsentsCalled();
        andResponseIsEmpty();

        thenConsentRetrievedIsReported();
        andConsentListContainsNumberOfItems(0);
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenFetchLatestConsentsCalled();
        andResponseFailsWithError(new ConsentNetworkError(new VolleyError()));

        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentDefinitions(new ConsentDefinition("text", "help", Collections.singletonList("moment"), 0, Locale.getDefault()));
        whenFetchLatestConsentsCalled();
        andResponseIs(new BackendConsent(Locale.CANADA, ConsentStatus.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsNumberOfItems(1);
    }

    @Test
    public void getStatusForConsentType_filtersByType() {
        givenAccessToolkitWithConsentDefinitions(
                new ConsentDefinition("text1", "help1", Collections.singletonList("type1"), 0, Locale.US),
                new ConsentDefinition("text2", "help2", Collections.singletonList("type2"), 0, Locale.US));
        whenGetStatusForConsentType("type2");
        thenCatkdgetStatusForConsentTypeWasCalledWith("type2");
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
        subject = new ConsentInteractor(mockCatk);
    }

    private void givenConsentDefinition(Locale locale) {
        givenConsentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("moment"), 0, locale);
    }

    private void whenCallingCreateConsentInGivenState(boolean checked) {
        subject.post(givenConsentDefinition, checked, mockPostConsentCallback);
    }

    private void thenCreateConsentIsCalledOnTheCatk() {
        verify(mockCatk).createConsent(Collections.singletonList(captorConsent.capture()), isA(CreateConsentListener.class));
    }

    private void whenGetStatusForConsentType(String type) {
        subject.getStatusForConsentType(type, consentCallback);
    }

    private void thenCatkdgetStatusForConsentTypeWasCalledWith(String expectedType) {
        verify(mockContentAccessToolkit).getStatusForConsentType(captorString.capture(), any(Integer.class), any(ConsentResponseListener.class));
        assertEquals(expectedType, captorString.getValue());
    }

    private void givenAccessToolkitWithConsentDefinitions(ConsentDefinition... consentDefinitions) {
        List<ConsentDefinition> givenConsentDefinitions = new ArrayList<>(consentDefinitions.length);
        Collections.addAll(givenConsentDefinitions, consentDefinitions);
        when(mockContentAccessToolkit.getConsentDefinitions()).thenReturn(Arrays.asList(consentDefinitions));
        subject = new ConsentInteractor(mockContentAccessToolkit);
    }

    private void whenFetchLatestConsentsCalled() {
        subject.fetchLatestConsents(mockCheckConsentsCallback);
    }

    private void whenCheckConsentsCalled() {
        subject.checkConsents(mockCheckConsentsCallback);
    }

    private void andResponseFailsWithError(ConsentNetworkError error) {
        verify(mockContentAccessToolkit).getConsentDetails(captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseFailureConsent(error);
    }

    private void andResponseIsEmpty() {
        andResponseIs();
    }

    private void andResponseIs(BackendConsent... response) {
        verify(mockContentAccessToolkit).getConsentDetails(captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseSuccessConsent(Arrays.asList(response));
    }

    private void thenGetConsentDetailsIsCalled() {
        verify(mockContentAccessToolkit).getConsentDetails(isA(ConsentInteractor.GetConsentsResponseListener.class));
    }

    private void thenConsentFailedIsReported() {
        verify(mockCheckConsentsCallback).onGetConsentsFailed(any(ConsentError.class));
    }

    private void thenConsentRetrievedIsReported() {
        verify(mockCheckConsentsCallback).onGetConsentsSuccess(captorRequired.capture());
    }

    private void andConsentListContainsNumberOfItems(int expectedNumberOfItems) {
        assertEquals(expectedNumberOfItems, captorRequired.getValue().size());
    }


    private ConsentCallback consentCallback = new ConsentCallback() {

        public Consent receivedRequiredConsent;
        public ConsentError receivedError;

        @Override
        public void onGetConsentRetrieved(@NonNull Consent consent) {
            this.receivedRequiredConsent = consent;
        }

        @Override
        public void onGetConsentFailed(ConsentError error) {
            this.receivedError = error;
        }
    };

}