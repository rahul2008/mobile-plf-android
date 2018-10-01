/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.UserLoginState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.catk.datamodel.CachedConsentStatus;
import com.philips.platform.catk.datamodel.ConsentDTO;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mock.LoggingInterfaceMock;
import com.philips.platform.catk.mock.RestInterfaceMock;
import com.philips.platform.catk.utils.CatkLogger;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {

    private static final boolean REACHABLE = true;
    private static final boolean UNREACHABLE = false;

    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;

    @Mock
    private ConsentsClient mockCatk;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    private ConsentCacheInterface mockCacheConsent;

    @Mock
    private User mockUser;

    @Captor
    private ArgumentCaptor<ConsentDTO> captorConsent;

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private TimeInterface timeInterface;

    @Mock
    private InternationalizationInterface internationalizationMock;

    @Captor
    private ArgumentCaptor<ConsentResponseListener> captorConsentDetails;

    @Captor
    private ArgumentCaptor<ConsentStatus> captorRequired;

    @Mock
    private ConsentCacheInteractor consentCacheInteractorMock;

    private static String someTimestamp = "2018-06-08T11:11:11.000Z";
    private static final String MOMENT_CONSENT = "moment";
    private static final String TEST_CONSENT = "TEST";
    private static final CachedConsentStatus VALID_CACHED_REJECTED_STATUS = new CachedConsentStatus(ConsentStates.rejected, 1, new DateTime().toDate(), new DateTime().plusHours(1));
    private static final CachedConsentStatus CACHED_REJECTED_STATUS_EXPIRED = new CachedConsentStatus(ConsentStates.rejected, 1, new DateTime().toDate(), new DateTime());
    private ConsentInteractor interactor;
    String versionMismatchErrorResponse = "{\"incidentID\":\"8bbaa45f-18db-4285-844f-68e72165eec6\",\"errorCode\":1252,\"description\":\"Cannot store lower version on top of higher version\"}";
    String someErrorResponse = "{\"incidentID\":\"8bbaa45f-18db-4285-844f-68e72165eec6\",\"errorCode\":100,\"description\":\"Cannot store lower version on top of higher version\"}";
    private RestInterfaceMock restInterfaceMock = new RestInterfaceMock();



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());

        when(mockCatk.getAppInfra()).thenReturn(appInfraMock);
        when(mockCatk.getAppInfra().getTime()).thenReturn(timeInterface);
        when(mockCatk.getAppInfra().getRestClient()).thenReturn(restInterfaceMock);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationMock);

        interactor = new ConsentInteractor(mockCatk, consentCacheInteractorMock);
    }

    @Test
    public void fetchConsentTypeStateFails_ifUserNotLoggedIn() {
        givenUserLoginStateIs(UserLoginState.USER_NOT_LOGGED_IN);
        givenInternetIs(REACHABLE);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        thenOnGetConsentsFailedIsCalledForFetchCallback();
    }

    @Test
    public void fetchConsentTypeState_whenOnline() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        givenInternetIs(REACHABLE);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        thenGetStatusForIsCalledFor(MOMENT_CONSENT);
    }

    @Test
    public void storeConsentTypeState_whenOnline() {
        givenInternetIs(REACHABLE);
        whenStoreConsentTypeStateIsCalled(MOMENT_CONSENT, true);
        thenCreateConsentIsCalledOnTheCatk();
    }

    @Test
    public void fetchConsentTypeState_whenOffline() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        givenInternetIs(UNREACHABLE);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        thenOnGetConsentsFailedIsCalledForFetchCallback();
    }

    @Test
    public void storeConsentTypeState_whenOffline() {
        givenInternetIs(UNREACHABLE);
        whenStoreConsentTypeStateIsCalled(MOMENT_CONSENT, true);
        thenOnGetConsentsFailedIsCalledForPostCallback();
    }

    @Test
    public void itShouldReportConsentFailedWhenResponseFails() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        andCatkResponseFailsWithError(new ConsentNetworkError(new VolleyError()));
        thenConsentFailedIsReported();
    }

    @Test
    public void itShouldReportConsentSuccessWhenNonEmptyResponse() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        andResponseFromCatkIs(new ConsentDTO("local", ConsentStates.active, "type", 0, new DateTime(someTimestamp)));
        thenConsentStatusReturnedInCallbackIs("active");
    }

    @Test
    public void storeConsentTypeState_CallsCacheConsentOnSuccess() {
        givenInternetIs(REACHABLE);
        whenStoreConsentTypeStateIsCalled(MOMENT_CONSENT, true);
        whenBackendStoreConsentReturnsSuccess();
        thenConsentCacheStoreIsCalledFor(MOMENT_CONSENT);
    }

    @Test
    public void storeConsentTypeState_DoesNotCallCacheConsentOnFailure() {
        givenInternetIs(REACHABLE);
        whenStoreConsentTypeStateIsCalled(MOMENT_CONSENT, true);
        whenBackendStoreConsentReturnsFailure();
        thenConsentCacheStoreIsNotCalled();
    }

    @Test
    public void fetchConsentTypeState_ReturnsCachedConsent() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        givenConsentCacheFetchReturns(VALID_CACHED_REJECTED_STATUS, TEST_CONSENT);
        whenFetchConsentStateIsCalledFor(TEST_CONSENT);
        thenGetStatusForConsentTypeIsNotCalled();
        thenConsentCacheFetchIsCalledFor(TEST_CONSENT);
        thenConsentStatusReturnedInCallbackIs("rejected");
    }

    @Test
    public void fetchConsentTypeState_CallsBackendFetch_IfCacheIsExpired() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        givenConsentCacheFetchReturns(CACHED_REJECTED_STATUS_EXPIRED, MOMENT_CONSENT);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);

        thenConsentCacheFetchIsCalledFor(MOMENT_CONSENT);

        thenGetStatusForIsCalledFor(MOMENT_CONSENT);
        andResponseFromCatkIs(new ConsentDTO("local", ConsentStates.active, "type", 0, new DateTime(someTimestamp)));

        thenConsentStatusReturnedInCallbackIs("active");
    }

    @Test
    public void fetchConsentTypeState_StoresIntoCache_IfFetchingFromBackend() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);

        thenConsentCacheFetchIsCalledFor(MOMENT_CONSENT);

        thenGetStatusForIsCalledFor(MOMENT_CONSENT);
        andResponseFromCatkIs(new ConsentDTO("local", ConsentStates.active, MOMENT_CONSENT, 1, new DateTime(someTimestamp)));

        thenConsentStatusReturnedInCallbackIs("active");
        thenConsentCacheStoreIsCalledFor(MOMENT_CONSENT);
    }

    @Test
    public void fetchConsentTypeState_returnsFromCacheEvenIfItsExpired_IfOffline() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        givenInternetIs(UNREACHABLE);
        givenConsentCacheFetchReturns(CACHED_REJECTED_STATUS_EXPIRED, MOMENT_CONSENT);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);

        thenConsentCacheFetchIsCalledFor(MOMENT_CONSENT);
        thenGetStatusForConsentTypeIsNotCalled();

        thenConsentStatusReturnedInCallbackIs("rejected");
    }

    @Test
    public void fetchConsentTypeState_DoesNotStoreToCache_IfBackendFetchThrowsError() {
        givenUserLoginStateIs(UserLoginState.USER_LOGGED_IN);
        whenFetchConsentStateIsCalledFor(MOMENT_CONSENT);
        andCatkResponseFailsWithError(new ConsentNetworkError(new VolleyError()));
        thenConsentFailedIsReported();
        thenConsentCacheStoreIsNotCalled();
    }

    @Test
    public void storeConsents_clearsCacheOnVersionMismatchError() {
        whenStoreConsentTypeStateIsCalled("consentType", true);
        thenBackendStoreConsentFailsWith(versionMismatchErrorResponse);
        thenConsentCacheIsClearedFor("consentType");
    }

    @Test
    public void storeConsents_doesNotclearCacheForOtherErrors() {
        whenStoreConsentTypeStateIsCalled("consentType", true);
        thenBackendStoreConsentFailsWith(someErrorResponse);
        thenConsentCacheClearIsNotCalled();
    }

    private void givenUserLoginStateIs(UserLoginState loggedIn) {
        when(mockUser.getUserLoginState()).thenReturn(loggedIn);
        when(mockCatkComponent.getUser()).thenReturn(mockUser);
        when(mockCatk.getCatkComponent()).thenReturn(mockCatkComponent);
    }

    private void givenConsentCacheFetchReturns(CachedConsentStatus cachedConsentStatus, String forConsentType) {
        when(consentCacheInteractorMock.fetchConsentTypeState(forConsentType)).thenReturn(cachedConsentStatus);
    }

    private void whenBackendStoreConsentReturnsSuccess() {
        ArgumentCaptor<CreateConsentListener> argumentCaptor = ArgumentCaptor.forClass(CreateConsentListener.class);
        verify(mockCatk).createConsent((ConsentDTO) any(), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess();
    }

    private void whenBackendStoreConsentReturnsFailure() {
        ArgumentCaptor<CreateConsentListener> argumentCaptor = ArgumentCaptor.forClass(CreateConsentListener.class);
        verify(mockCatk).createConsent((ConsentDTO) any(), argumentCaptor.capture());
        argumentCaptor.getValue().onFailure(new ConsentNetworkError(new VolleyError()));
    }

    private void thenBackendStoreConsentFailsWith(String errorResponse) {
        ArgumentCaptor<CreateConsentListener> argumentCaptor = ArgumentCaptor.forClass(CreateConsentListener.class);
        verify(mockCatk).createConsent((ConsentDTO) any(), argumentCaptor.capture());
        argumentCaptor.getValue().onFailure(new ConsentNetworkError(new ServerError(new NetworkResponse(errorResponse.getBytes()))));
    }

    private void whenFetchConsentStateIsCalledFor(String consentType) {
        interactor.fetchConsentTypeState(consentType, fetchConsentTypeStateCallback);
    }

    private void whenStoreConsentTypeStateIsCalled(String consentType, boolean status) {
        when(mockCatk.getAppInfra().getTime().getUTCTime()).thenReturn(new DateTime(someTimestamp).toDate());
        interactor.storeConsentTypeState(consentType, status, 1, postConsentTypeCallback);
    }


    private void givenInternetIs(boolean isInternetAvailable) {
        restInterfaceMock.isInternetAvailable = isInternetAvailable;
    }

    private void thenGetStatusForIsCalledFor(String expectedConsentType) {
        verify(mockCatk).getStatusForConsentType(eq(expectedConsentType), isA(ConsentResponseListener.class));
    }

    private void thenGetStatusForConsentTypeIsNotCalled() {
        verify(mockCatk, never()).getStatusForConsentType(eq(MOMENT_CONSENT), isA(ConsentResponseListener.class));
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

    private void thenConsentCacheStoreIsCalledFor(String expectedConsentType) {
        verify(consentCacheInteractorMock).storeConsentState(expectedConsentType, ConsentStates.active, 1, new DateTime(someTimestamp).toDate());
    }

    private void thenConsentCacheFetchIsCalledFor(String consentType) {
        verify(consentCacheInteractorMock).fetchConsentTypeState(consentType);
    }

    private void thenConsentCacheStoreIsNotCalled() {
        verify(consentCacheInteractorMock, never()).storeConsentState(MOMENT_CONSENT, ConsentStates.active, 1, new DateTime(someTimestamp).toDate());
    }

    private void andCatkResponseFailsWithError(ConsentNetworkError error) {
        verify(mockCatk).getStatusForConsentType(eq(MOMENT_CONSENT), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseFailureConsent(error);
    }

    private void thenConsentFailedIsReported() {
        verify(fetchConsentTypeStateCallback).onGetConsentsFailed(any(ConsentError.class));
    }

    private void andResponseFromCatkIs(ConsentDTO... response) {
        verify(mockCatk).getStatusForConsentType(eq(MOMENT_CONSENT), captorConsentDetails.capture());
        captorConsentDetails.getValue().onResponseSuccessConsent(Arrays.asList(response));
    }

    private void thenConsentStatusReturnedInCallbackIs(String expectedStatus) {
        verify(fetchConsentTypeStateCallback).onGetConsentsSuccess(captorRequired.capture());
        assertEquals(expectedStatus, captorRequired.getValue().getConsentState().name());
    }

    private void thenConsentCacheIsClearedFor(String expectedConsentType) {
        verify(consentCacheInteractorMock).clearCache(expectedConsentType);
    }

    private void thenConsentCacheClearIsNotCalled() {
        verify(consentCacheInteractorMock, times(0)).clearCache(anyString());
    }
}