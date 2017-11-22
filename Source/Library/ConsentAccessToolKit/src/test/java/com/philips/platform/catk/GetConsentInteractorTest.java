/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.catk.mock.LoggingInterfaceMock;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.ConsentDefinition;
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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetConsentInteractorTest {

    GetConsentInteractor subject;

    @Mock private ConsentAccessToolKit mockContentAccessToolkit;
    @Mock private GetConsentInteractor.Callback mockCallback;
    @Captor private ArgumentCaptor<GetConsentInteractor.ConsentViewResponseListener> captorConsentDetails;
    @Captor private ArgumentCaptor<Map<String, RequiredConsent>> captorRequiredMap;

    private List<ConsentDefinition> givenConsentDefinitions;

    @Before
    public void setUp() throws Exception {
       CatkLogger.disableLogging();
       CatkLogger.setLoggerInterface(new LoggingInterfaceMock());
    }

    @Test
    public void itShouldGetConsentDetails() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenGetConsentCalled();
        thenGetConsentDetailsIsCalled();
    }

    @Test
    public void itShouldReportConsentFailedWhenEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenGetConsentCalled();
        andResponseIsEmpty();

        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenGetConsentCalled();
        andResponseFailsWithError(400);

        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentDefinitions(new ConsentDefinition("text", "help", "type", 0, Locale.getDefault()));
        whenGetConsentCalled();
        andResponseIs(new Consent(Locale.CANADA, ConsentStatus.active, "type", 0));

        thenConsentRetrievedIsReported();
        assertEquals(1, captorRequiredMap.getValue().size());
    }

    private void givenAccessToolkitWithConsentDefinitions(ConsentDefinition... consentDefinitions) {
        givenConsentDefinitions = new ArrayList<>(consentDefinitions.length);
        Collections.addAll(givenConsentDefinitions, consentDefinitions);
        subject = new GetConsentInteractor(mockContentAccessToolkit, givenConsentDefinitions);
    }

    private void whenGetConsentCalled() {
        subject.getConsents(mockCallback);
    }

    private void andResponseFailsWithError(int error) {
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
        verify(mockCallback).onConsentFailed(anyInt());
    }

    private void thenConsentRetrievedIsReported() {
        verify(mockCallback).onConsentRetrieved(captorRequiredMap.capture());
    }

    @NonNull
    private LoggingInterface emptyLogger() {
        return new LoggingInterface() {
            @Override
            public LoggingInterface createInstanceForComponent(String s, String s1) {
                return this;
            }

            @Override
            public void log(LogLevel logLevel, String s, String s1) {

            }

            @Override
            public void log(LogLevel logLevel, String s, String s1, Map<String, ?> map) {

            }
        };
    }

}