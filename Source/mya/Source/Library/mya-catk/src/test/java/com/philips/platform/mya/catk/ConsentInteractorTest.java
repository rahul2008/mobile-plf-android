/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.listener.GetConsentsResponseListener;
import com.philips.platform.mya.catk.mock.LoggingInterfaceMock;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import android.support.annotation.NonNull;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {
    private static final String CANADIAN_LOCALE = "-CA";
    ConsentInteractor subject;

    @Mock
    private ConsentsClient mockContentAccessToolkit;
    @Mock
    private CheckConsentsCallback mockCheckConsentsCallback;
    @Captor
    private ArgumentCaptor<GetConsentsResponseListener> captorConsentDetails;
    @Captor
    private ArgumentCaptor<List<Consent>> captorRequired;
    @Captor
    private ArgumentCaptor<String> captorString;
    @Mock
    private ConsentsClient mockCatk;
    private ConsentDefinition givenConsentDefinition;
    @Captor
    private ArgumentCaptor<BackendConsent> captorConsent;
    @Mock
    private PostConsentCallback mockPostConsentCallback;
    @Mock
    private AppInfraInterface appInfraMock;
    @Mock
    private InternationalizationInterface internationalizationMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());

        when(mockCatk.getAppInfra()).thenReturn(appInfraMock);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationMock);
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
        givenAccessToolkitWithConsentDefinitions(new ConsentDefinition("text", "help", Collections.singletonList("moment"), 0));
        whenFetchLatestConsentsCalled();
        andResponseIs(new BackendConsent(CANADIAN_LOCALE, ConsentStatus.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsNumberOfItems(1);
    }

    @Test
    public void getStatusForConsentType_filtersByType() {
        givenAccessToolkitWithConsentDefinitions(
                new ConsentDefinition("text1", "help1", Collections.singletonList("type1"), 0),
                new ConsentDefinition("text2", "help2", Collections.singletonList("type2"), 0));
        whenGetStatusForConsentType("type2");
        thenCatkdgetStatusForConsentTypeWasCalledWith("type2");
    }

    @Test
    public void itShouldCallCreateConsentOnTheCatk() throws Exception {
        when(internationalizationMock.getBCP47UILocale()).thenReturn("en-US");
        givenCreateConsentInteractor();
        givenConsentDefinition();
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test()
    public void itShouldThrowConsentDefinitionExceptionWhenUsingDefinitionWithLocaleThatIsMissingCountry() throws Exception {
        when(internationalizationMock.getBCP47UILocale()).thenReturn("en-");
        givenCreateConsentInteractor();
        givenConsentDefinition();
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test()
    public void itShouldThrowConsentDefinitionExceptionWhenUsingDefinitionWithLocaleThatIsMissingLanguage() throws Exception {
        when(internationalizationMock.getBCP47UILocale()).thenReturn("-US");
        givenCreateConsentInteractor();
        givenConsentDefinition();
        whenCallingCreateConsentInGivenState(true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    private void givenCreateConsentInteractor() {
        subject = new ConsentInteractor(mockCatk);
    }

    private void givenConsentDefinition() {
        givenConsentDefinition = new ConsentDefinition("text", "help", Collections.singletonList("moment"), 0);
    }

    private void whenCallingCreateConsentInGivenState(boolean checked) {
        subject.storeConsentState(givenConsentDefinition, checked, mockPostConsentCallback);
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
        subject.fetchConsentStates(null, mockCheckConsentsCallback);
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
        verify(mockContentAccessToolkit).getConsentDetails(isA(GetConsentsResponseListener.class));
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