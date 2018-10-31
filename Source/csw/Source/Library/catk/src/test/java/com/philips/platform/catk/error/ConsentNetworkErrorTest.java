/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.catk.error;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.philips.platform.pif.chi.ConsentError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ConsentNetworkErrorTest {

    private ConsentNetworkError consentNetworkError;

    @Mock
    private NoConnectionError mockNoConnectionError;

    @Mock
    com.android.volley.ServerError mockServerError;

    @Mock
    AuthFailureError mockAuthFailureError;

    @Mock
    TimeoutError mockTimeoutError;

    @Mock
    VolleyError mockVolleyError;

    private String someErrorResponse = "{\"incidentID\":\"8bbaa45f\",\"errorCode\":100,\"description\":\"description\"}";;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        consentNetworkError = null;
    }

    @Test
    public void testInit() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockVolleyError);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_UNKNOWN);
    }

    @Test
    public void tesGetMessage() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockTimeoutError);
        whenSettingErrorMessage(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
        thenErrorMessageIs(CUSTOM_ERROR_MESSAGE);
    }

    @Test
    public void tesGetMessageServerError() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockTimeoutError);
        whenSettingErrorCode(ConsentError.CONSENT_ERROR_NO_CONNECTION);
        whenSettingErrorMessage(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_NO_CONNECTION);
        thenErrorMessageIs(CUSTOM_ERROR_MESSAGE);
    }

    @Test
    public void tesGetStatusCode() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockTimeoutError);
        whenSettingErrorCode(ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
        whenSettingErrorMessage(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
        thenStatusCodeIs(ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    @Test
    public void testSuccessCode() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockTimeoutError);
        whenSettingErrorCode(ConsentError.CONSENT_SUCCESS);
        whenSettingErrorMessage(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_SUCCESS);
        thenStatusCodeIs(ConsentError.CONSENT_SUCCESS);
    }

    @Test
    public void testNoConnectionErrorType() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockNoConnectionError);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_NO_CONNECTION);
    }

    @Test
    public void testServerError_setsErrorCodeFromServerError() throws Exception {
        whenCreatingConsentNetworkErrorWithType(new ServerError(new NetworkResponse(someErrorResponse.getBytes())));
        thenConsentNetworkErrorCodeIs(100);
    }

    @Test
    public void testServerError_setsErrorCode5WhenNoNetworkResponse() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockServerError);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_UNKNOWN_SERVER_ERROR);
    }

    @Test
    public void testAuthFailureErrorType() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockAuthFailureError);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_AUTHENTICATION_FAILURE);
    }

    @Test
    public void testTimeOutErrorType() throws Exception {
        whenCreatingConsentNetworkErrorWithType(mockTimeoutError);
        thenConsentNetworkErrorCodeIs(ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    private void whenCreatingConsentNetworkErrorWithType(VolleyError error) {
        consentNetworkError = new ConsentNetworkError(error);
    }

    private void whenSettingErrorMessage(String errorMessage) {
        consentNetworkError.setCustomErrorMessage(errorMessage);
    }

    private void whenSettingErrorCode(int errorCode) {
        consentNetworkError.setCatkErrorCode(errorCode);
    }

    private void thenConsentNetworkErrorCodeIs(int errorCode) {
        assertEquals(errorCode, consentNetworkError.getCatkErrorCode());
    }

    private void thenErrorMessageIs(String errorMessage) {
        assertEquals(errorMessage, consentNetworkError.getMessage());
    }

    private void thenStatusCodeIs(int statusCode) {
        assertEquals(statusCode, consentNetworkError.getStatusCode());
    }

    private static final String CUSTOM_ERROR_MESSAGE = "markErrorAndGetPrevious";
}