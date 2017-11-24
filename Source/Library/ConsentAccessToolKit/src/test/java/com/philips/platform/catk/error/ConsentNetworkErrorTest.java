/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(CustomRobolectricRunnerCATK.class)
@Config(constants = BuildConfig.class, sdk = 25)
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
        givenConsentNetworkErrorWithType(mockVolleyError);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_UNKNOWN);
    }

    @Test
    public void tesGetMessage() throws Exception {
        givenConsentNetworkErrorWithType(mockTimeoutError);
        givenCustomErrorMessageIs(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT);
        thenErrorMessageIs(CUSTOM_ERROR_MESSAGE);
    }

    @Test
    public void tesGetMessageServerError() throws Exception {
        givenConsentNetworkErrorWithType(mockTimeoutError);
        givenErrorCodeIs(CatkConstants.CONSENT_ERROR_NO_CONNECTION);
        givenCustomErrorMessageIs(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_NO_CONNECTION);
        thenErrorMessageIs(CUSTOM_ERROR_MESSAGE);
    }

    @Test
    public void tesGetStatusCode() throws Exception {
        givenConsentNetworkErrorWithType(mockTimeoutError);
        givenErrorCodeIs(CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT);
        givenCustomErrorMessageIs(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT);
        thenStatusCodeIs(CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    @Test
    public void testSuccessCode() throws Exception {
        givenConsentNetworkErrorWithType(mockTimeoutError);
        givenErrorCodeIs(CatkConstants.CONSENT_SUCCESS);
        givenCustomErrorMessageIs(CUSTOM_ERROR_MESSAGE);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_SUCCESS);
        thenStatusCodeIs(CatkConstants.CONSENT_SUCCESS);
    }

    @Test
    public void testNoConnectionErrorType() throws Exception {
        givenConsentNetworkErrorWithType(mockNoConnectionError);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_NO_CONNECTION);
    }

    @Test
    public void testServerErrorErrorType() throws Exception {
        givenConsentNetworkErrorWithType(mockServerError);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_SERVER_ERROR);
    }

    @Test
    public void testAuthFailureErrorType() throws Exception {
        givenConsentNetworkErrorWithType(mockAuthFailureError);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_AUTHENTICATION_FAILURE);
    }

    @Test
    public void testTimeOutErrorType() throws Exception {
        givenConsentNetworkErrorWithType(mockTimeoutError);
        thenConsentNetworkErrorCodeIs(CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    private void givenConsentNetworkErrorWithType(VolleyError error) {
        consentNetworkError = new ConsentNetworkError(error);
    }

    private void givenCustomErrorMessageIs(String errorMessage) {
        consentNetworkError.setCustomErrorMessage(errorMessage);
    }

    private void givenErrorCodeIs(int errorCode) {
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