/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.android.volley.VolleyError;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.mock.LoggingInterfaceMock;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.RequiredConsent;
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

@RunWith(MockitoJUnitRunner.class)
public class GetConsentInteractorTest {

    GetConsentInteractor subject;

    @Mock
    private ConsentAccessToolKit mockContentAccessToolkit;
    @Mock
    private GetConsentInteractor.Callback mockCallback;
    @Captor
    private ArgumentCaptor<GetConsentInteractor.ConsentViewResponseListener> captorConsentDetails;
    @Captor
    private ArgumentCaptor<List<RequiredConsent>> captorRequired;

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
        andResponseIs(new Consent(Locale.CANADA, ConsentStatus.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsNumberOfItems(1);
    }

    private void givenAccessToolkitWithConsentDefinitions(ConsentDefinition... consentDefinitions) {
        List<ConsentDefinition> givenConsentDefinitions = new ArrayList<>(consentDefinitions.length);
        Collections.addAll(givenConsentDefinitions, consentDefinitions);
        subject = new GetConsentInteractor(mockContentAccessToolkit, givenConsentDefinitions);
    }

    private void whenFetchLatestConsentsCalled() {
        subject.fetchLatestConsents(mockCallback);
    }

    private void whenCheckConsentsCalled() {
        subject.checkConsents(mockCallback);
    }

    private void andResponseFailsWithError(ConsentNetworkError error) {
        verify(mockContentAccessToolkit).getConsentDetails(captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseFailureConsent(error);
    }

    private void andResponseIsEmpty() {
        andResponseIs();
    }

    private void andResponseIs(Consent... response) {
        verify(mockContentAccessToolkit).getConsentDetails(captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseSuccessConsent(Arrays.asList(response));
    }

    private void thenGetConsentDetailsIsCalled() {
        verify(mockContentAccessToolkit).getConsentDetails(isA(GetConsentInteractor.ConsentViewResponseListener.class));
    }

    private void thenConsentFailedIsReported() {
        verify(mockCallback).onGetConsentFailed(any(ConsentNetworkError.class));
    }

    private void thenConsentRetrievedIsReported() {
        verify(mockCallback).onGetConsentRetrieved(captorRequired.capture());
    }

    private void andConsentListContainsNumberOfItems(int expectedNumberOfItems) {
        assertEquals(expectedNumberOfItems, captorRequired.getValue().size());
    }

}