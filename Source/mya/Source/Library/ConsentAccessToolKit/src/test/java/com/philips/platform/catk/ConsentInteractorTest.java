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
import com.philips.platform.catk.mock.LoggingInterfaceMock;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.utils.CatkLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
    private ConsentInteractor.ConsentListCallback mockConsentListCallback;
    @Captor
    private ArgumentCaptor<ConsentInteractor.GetConsentsResponseListener> captorConsentDetails;
    @Captor
    private ArgumentCaptor<List<Consent>> captorRequired;
    @Captor
    private ArgumentCaptor<String> captorString;

    @Before
    public void setUp() throws Exception {
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
        subject.fetchLatestConsents(mockConsentListCallback);
    }

    private void whenCheckConsentsCalled() {
        subject.checkConsents(mockConsentListCallback);
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
        verify(mockConsentListCallback).onGetConsentFailed(any(ConsentNetworkError.class));
    }

    private void thenConsentRetrievedIsReported() {
        verify(mockConsentListCallback).onGetConsentRetrieved(captorRequired.capture());
    }

    private void andConsentListContainsNumberOfItems(int expectedNumberOfItems) {
        assertEquals(expectedNumberOfItems, captorRequired.getValue().size());
    }


    private ConsentInteractor.ConsentCallback consentCallback = new ConsentInteractor.ConsentCallback() {

        public Consent receivedRequiredConsent;
        public ConsentNetworkError receivedError;

        @Override
        public void onGetConsentRetrieved(@NonNull Consent consent) {
            this.receivedRequiredConsent = consent;
        }

        @Override
        public void onGetConsentFailed(ConsentNetworkError error) {
            this.receivedError = error;
        }
    };

}