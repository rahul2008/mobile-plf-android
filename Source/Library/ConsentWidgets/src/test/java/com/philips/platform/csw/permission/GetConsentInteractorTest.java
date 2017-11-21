package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.utils.CswLogger;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetConsentInteractorTest {

    GetConsentInteractor subject;

    @Mock private ConsentAccessToolKit mockContentAccessToolkit;
    @Mock private GetConsentInteractor.Callback mockCallback;
    @Captor private ArgumentCaptor<GetConsentInteractor.ConsentViewResponseListener> captorConsentDetails;
    @Captor private ArgumentCaptor<List<ConsentView>> captorConsentRetrieved;

    private List<ConsentView> givenConsentDefinitions;

    @Before
    public void setUp() throws Exception {
       CswLogger.disableLogging();
       CswLogger.setLoogerInterface(emptyLogger());
    }

    @Test
    public void itShouldGetConsentDetails() throws Exception {
        givenAccessToolkitWithConsentViews();
        whenGetConsentCalled();
        thenGetConsentDetailsIsCalled();
    }

    @Test
    public void itShouldReportConsentFailedWhenEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentViews();
        whenGetConsentCalled();
        andResponseIsEmpty();

        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() throws Exception {
        givenAccessToolkitWithConsentViews();
        whenGetConsentCalled();
        andResponseFailsWithError(400);

        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() throws Exception {
        givenAccessToolkitWithConsentViews(new ConsentView(new ConsentDefinition("text", "help", "type", 0, Locale.getDefault())));
        whenGetConsentCalled();
        andResponseIs(new Consent(Locale.CANADA, ConsentStatus.active, "type", 0));

        thenConsentRetrievedIsReported();
        assertEquals(1, captorConsentRetrieved.getValue().size());
    }

    private void givenAccessToolkitWithConsentViews(ConsentView... consentViews) {
        givenConsentDefinitions = new ArrayList<>(consentViews.length);
        Collections.addAll(givenConsentDefinitions, consentViews);
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
        verify(mockCallback).onConsentRetrieved(captorConsentRetrieved.capture());
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