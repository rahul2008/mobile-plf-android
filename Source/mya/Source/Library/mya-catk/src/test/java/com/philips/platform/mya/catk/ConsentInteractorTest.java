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
import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.mya.catk.datamodel.ConsentDTO;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mock.LoggingInterfaceMock;
import com.philips.platform.mya.catk.mock.RestInterfaceMock;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.joda.time.DateTime;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {
    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;

    @Mock
    private ConsentsClient mockCatk;

    @Mock
    private ConsentCacheInterface mockCacheConsent;

    @Captor
    private ArgumentCaptor<ConsentDTO> captorConsent;

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private InternationalizationInterface internationalizationMock;

    @Captor
    private ArgumentCaptor<ConsentResponseListener> captorConsentDetails;

    @Captor
    private ArgumentCaptor<ConsentStatus> captorRequired;

    @Mock
    private ConsentCacheInteractor consentCacheInteractorMock;

    private static final String CONSENT_TYPE = "moment";
    private static final CachedConsentStatus VALID_CACHED_REJECTED_STATUS = new CachedConsentStatus(ConsentStates.rejected, 1, new DateTime().plusHours(1));
    private static final CachedConsentStatus CACHED_REJECTED_STATUS_EXPIRED = new CachedConsentStatus(ConsentStates.rejected, 1, new DateTime());
    private ConsentInteractor interactor;
    private RestInterfaceMock restInterfaceMock = new RestInterfaceMock();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());

        when(mockCatk.getAppInfra()).thenReturn(appInfraMock);
        when(mockCatk.getAppInfra().getRestClient()).thenReturn(restInterfaceMock);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationMock);

        interactor = new ConsentInteractor(mockCatk, consentCacheInteractorMock);
    }

    @Test
    public void fetchConsentTypeState_whenOnilne() {
        whenAppIsOnline();
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);
        thenGetStatusForConsentTypeIsCalled();
    }

    @Test
    public void storeConsentTypeState_whenOnline() {
        whenAppIsOnline();
        whenStoreConsentTypeStateIsCalled(CONSENT_TYPE, true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test
    public void fetchConsentTypeState_whenOffline() {
        whenAppIsOffline();
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);
        thenOnGetConsentsFailedIsCalledForFetchCallback();
    }

    @Test
    public void storeConsentTypeState_whenOffline() {
        whenAppIsOffline();
        whenStoreConsentTypeStateIsCalled(CONSENT_TYPE, true);
        thenOnGetConsentsFailedIsCalledForPostCallback();
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() {
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);
        andResponseFailsWithError(new ConsentNetworkError(new VolleyError()));
        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() {
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);
        andResponseFromCatkIs(new ConsentDTO("local", ConsentStates.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsState("active");
    }

    @Test
    public void storeConsentTypeState_CallsCacheConsentOnSuccess() {
        whenAppIsOnline();
        whenStoreConsentTypeStateIsCalled(CONSENT_TYPE, true);
        whenBackendStoreConsentReturnsSuccess();
        thenConsentCacheStoreIsCalled();
    }

    @Test
    public void storeConsentTypeState_DoesNotCallCacheConsentOnFailure() {
        whenAppIsOnline();
        whenStoreConsentTypeStateIsCalled(CONSENT_TYPE, true);
        whenBackendStoreConsentReturnsFailure();
        thenConsentCacheStoreIsNotCalled();
    }

    @Test
    public void fetchConsentTypeState_ReturnsCachedConsent() {
        givenConsentCacheFetchReturns(VALID_CACHED_REJECTED_STATUS);
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);
        thenGetStatusForConsentTypeIsNotCalled();
        thenConsentCacheFetchIsCalled();
        thenConsentRetrievedIsReported();
        andConsentListContainsState("rejected");
    }

    @Test
    public void fetchConsentTypeState_CallsBackendFetch_IfCacheIsExpired() {
        givenConsentCacheFetchReturns(CACHED_REJECTED_STATUS_EXPIRED);
        whenFetchConsentTypeStateIsCalled(CONSENT_TYPE);

        thenConsentCacheFetchIsCalled();

        thenGetStatusForConsentTypeIsCalled();
        andResponseFromCatkIs(new ConsentDTO("local", ConsentStates.active, "type", 0));

        thenConsentRetrievedIsReported();
        andConsentListContainsState("active");
    }

    private void givenConsentCacheFetchReturns(CachedConsentStatus cachedConsentStatus) {
        when(consentCacheInteractorMock.fetchConsentTypeState(CONSENT_TYPE)).thenReturn(cachedConsentStatus);
    }

    private void whenBackendStoreConsentReturnsSuccess() {
        ArgumentCaptor<CreateConsentListener> argumentCaptor = ArgumentCaptor.forClass(CreateConsentListener.class);
        verify(mockCatk).createConsent((ConsentDTO) any(),argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess();
    }

    private void whenBackendStoreConsentReturnsFailure() {
        ArgumentCaptor<CreateConsentListener> argumentCaptor = ArgumentCaptor.forClass(CreateConsentListener.class);
        verify(mockCatk).createConsent((ConsentDTO) any(),argumentCaptor.capture());
        argumentCaptor.getValue().onFailure(new ConsentNetworkError(new VolleyError()));
    }

    private void whenFetchConsentTypeStateIsCalled(String consentType) {
        interactor.fetchConsentTypeState(consentType, fetchConsentTypeStateCallback);
    }

    private void whenStoreConsentTypeStateIsCalled(String consentType, boolean status) {
        interactor.storeConsentTypeState(consentType, status, 1, postConsentTypeCallback);
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

    private void thenGetStatusForConsentTypeIsNotCalled() {
        verify(mockCatk, never()).getStatusForConsentType(eq(CONSENT_TYPE), isA(ConsentResponseListener.class));
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

    private void thenConsentCacheStoreIsCalled() {
        verify(consentCacheInteractorMock).storeConsentTypeState(CONSENT_TYPE, ConsentStates.active, 1);
    }

    private void thenConsentCacheFetchIsCalled() {
        verify(consentCacheInteractorMock).fetchConsentTypeState(CONSENT_TYPE);
    }

    private void thenConsentCacheStoreIsNotCalled() {
        verify(consentCacheInteractorMock, never()).storeConsentTypeState(CONSENT_TYPE, ConsentStates.active, 1);
    }

    private void andResponseFailsWithError(ConsentNetworkError error) {
        verify(mockCatk).getStatusForConsentType(eq(CONSENT_TYPE), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseFailureConsent(error);
    }

    private void thenConsentFailedIsReported() {
        verify(fetchConsentTypeStateCallback).onGetConsentsFailed(any(ConsentError.class));
    }

    private void andResponseFromCatkIs(ConsentDTO... response) {
        verify(mockCatk).getStatusForConsentType(eq(CONSENT_TYPE), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseSuccessConsent(Arrays.asList(response));
    }

    private void thenConsentRetrievedIsReported() {
        verify(fetchConsentTypeStateCallback).onGetConsentsSuccess(captorRequired.capture());
    }

    private void andConsentListContainsState(String expectedStatus) {
        assertEquals(expectedStatus, captorRequired.getValue().getConsentState().name());
    }
}