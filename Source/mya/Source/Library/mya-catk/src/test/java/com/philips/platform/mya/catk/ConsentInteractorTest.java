/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mock.LoggingInterfaceMock;
import com.philips.platform.mya.catk.mock.RestInterfaceMock;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {

    @Test
    public void fetchConsentTypeState_whenOnilne() throws Exception {
        whenAppIsOnline();
        whenFetchConsentTypeStateIsCalled();
        thenGetStatusForConsentTypeIsCalled();
    }

    @Test
    public void storeConsentTypeState_whenOnline() throws Exception {
        whenAppIsOnline();
        whenStoreConsentTypeStateIsCalled();
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test
    public void fetchConsentTypeState_whenOffline() throws Exception {
        whenAppIsOffline();
        whenFetchConsentTypeStateIsCalled();
        thenOnGetConsentsFailedIsCalledForFetchCallback();
    }

    @Test
    public void storeConsentTypeState_whenOffline() throws Exception {
        whenAppIsOffline();
        whenStoreConsentTypeStateIsCalled();
        thenOnGetConsentsFailedIsCalledForPostCallback();
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() throws Exception {
        whenFetchConsentTypeStateIsCalled();
        andResponseFailsWithError(new ConsentNetworkError(new VolleyError()));
        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() throws Exception {
        whenFetchConsentTypeStateIsCalled();
        andResponseIs(new BackendConsent("local", ConsentStates.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsNumberOfItems(1);
    }

    private void whenFetchConsentTypeStateIsCalled() {
        interactor.fetchConsentTypeState(CONSENT_TYPE, fetchConsentTypeStateCallback);
    }

    private void whenStoreConsentTypeStateIsCalled() {
        interactor.storeConsentTypeState(CONSENT_TYPE, true, 1, postConsentTypeCallback);
    }


    private void whenAppIsOnline() {
        restInterfaceMock.isInternetAvailable = true;
    }

    private void whenAppIsOffline() {
        restInterfaceMock.isInternetAvailable = false;
    }

    private void thenGetStatusForConsentTypeIsCalled() {
        verify(mockCatk).getStatusForConsentType(eq(CONSENT_TYPE), isA(ConsentResponseListener.class));
    }

    private void thenCreateConsentIsCalledOnTheCatk() {
        verify(mockCatk).createConsent(captorConsent.capture(), isA(CreateConsentListener.class));
    }

    private void thenOnGetConsentsFailedIsCalledForFetchCallback() {
        verify(fetchConsentTypeStateCallback).onGetConsentsFailed(any(ConsentError.class));
    }

    private void thenOnGetConsentsFailedIsCalledForPostCallback() {
        verify(postConsentTypeCallback).onPostConsentFailed(any(ConsentError.class));
    }

    private void andResponseFailsWithError(ConsentNetworkError error) {
        verify(mockCatk).getStatusForConsentType(eq(CONSENT_TYPE), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseFailureConsent(error);
    }

    private void thenConsentFailedIsReported() {
        verify(fetchConsentTypeStateCallback).onGetConsentsFailed(any(ConsentError.class));
    }

    private void andResponseIs(BackendConsent... response) {
        verify(mockCatk).getStatusForConsentType(eq(CONSENT_TYPE), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseSuccessConsent(Arrays.asList(response));
    }

    private void thenConsentRetrievedIsReported() {
        verify(fetchConsentTypeStateCallback).onGetConsentsSuccess(captorRequired.capture());
    }

    private void andConsentListContainsNumberOfItems(int expectedNumberOfItems) {
        assertEquals("active", captorRequired.getValue().getConsentState().name());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());

        when(mockCatk.getAppInfra()).thenReturn(appInfraMock);
        when(mockCatk.getAppInfra().getRestClient()).thenReturn(restInterfaceMock);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationMock);

        interactor = new ConsentInteractor(mockCatk);
    }

    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;

    @Mock
    private ConsentsClient mockCatk;

    @Captor
    private ArgumentCaptor<BackendConsent> captorConsent;

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private InternationalizationInterface internationalizationMock;

    @Captor
    private ArgumentCaptor<ConsentResponseListener> captorConsentDetails;

    @Captor
    private ArgumentCaptor<ConsentStatus> captorRequired;

    private static final String CONSENT_TYPE = "moment";
    private ConsentInteractor interactor;
    private RestInterfaceMock restInterfaceMock = new RestInterfaceMock();

}