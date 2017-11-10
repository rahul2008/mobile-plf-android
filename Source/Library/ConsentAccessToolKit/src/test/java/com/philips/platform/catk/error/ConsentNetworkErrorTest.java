/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

import static junit.framework.Assert.assertEquals;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import com.android.volley.*;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

@RunWith(CustomRobolectricRunnerCATK.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ConsentNetworkErrorTest {

    private ConsentNetworkError consentNetworkError;

    @Mock
    private NoConnectionError mockNoConnectionError;

    @Mock
    RequestListener mockRequestListener;

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
        consentNetworkError = new ConsentNetworkError(mockVolleyError, 3, mockRequestListener);
        consentNetworkError.initMessage(1, mockRequestListener);
    }

    @Test
    public void tesGetMessage() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError, 3, mockRequestListener);
        consentNetworkError.initMessage(1, mockRequestListener);
        consentNetworkError.setCustomErrorMessage(CUSTOM_ERROR_MESSAGE);
        assertEquals(CUSTOM_ERROR_MESSAGE, consentNetworkError.getMessage());
    }

    @Test
    public void tesGetMessageServerError() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError, 3, mockRequestListener);
        consentNetworkError.initMessage(5, mockRequestListener);
        consentNetworkError.setErrorCode(2);
        consentNetworkError.setCustomErrorMessage(CUSTOM_ERROR_MESSAGE);
        Assert.assertNotNull(consentNetworkError.getMessage());
    }

    @Test
    public void tesGetStatusCode() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError, 3, mockRequestListener);
        consentNetworkError.initMessage(5, mockRequestListener);
        consentNetworkError.setErrorCode(3);
        consentNetworkError.setCustomErrorMessage(CUSTOM_ERROR_MESSAGE);
        assertEquals(3, consentNetworkError.getStatusCode());
    }

    @Test
    public void testSuccessCode() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError, 3, mockRequestListener);
        consentNetworkError.initMessage(5, mockRequestListener);
        consentNetworkError.setErrorCode(CatkConstants.CONSENT_SUCCESS);
        consentNetworkError.setCustomErrorMessage(CUSTOM_ERROR_MESSAGE);
        assertEquals(CatkConstants.CONSENT_SUCCESS, consentNetworkError.getStatusCode());
    }

    @Test
    public void testNoConnectionErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockNoConnectionError, 3, mockRequestListener);
        consentNetworkError.initMessage(2, mockRequestListener);
    }

    @Test
    public void testServerErrorErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockServerError, 3, mockRequestListener);
        consentNetworkError.initMessage(3, mockRequestListener);
    }

    @Test
    public void testAuthFailureErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockAuthFailureError, 3, mockRequestListener);
        consentNetworkError.initMessage(4, mockRequestListener);
    }

    @Test
    public void testTimeOutErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError, 3, mockRequestListener);
        consentNetworkError.initMessage(5, mockRequestListener);
    }

    private static final String CUSTOM_ERROR_MESSAGE = "error";
}