package com.philips.platform.catk.error;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.annotation.Config;

/**
 * Created by Maqsood on 11/4/17.
 */

@RunWith(CustomRobolectricRunnerCATK.class)
@Config(constants = BuildConfig.class, sdk = 25)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
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
        consentNetworkError = new ConsentNetworkError(mockVolleyError,3,mockRequestListener);
        consentNetworkError.initMessage(1,mockRequestListener);
    }

    @Test
    public void tesGetMessage() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError,3,mockRequestListener);
        consentNetworkError.initMessage(1,mockRequestListener);
        consentNetworkError.setCustomErrorMessage("server","error");
        Assert.assertNotNull(consentNetworkError.getMessage());
    }

    @Test
    public void tesGetMessageServerError() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError,3,mockRequestListener);
        consentNetworkError.initMessage(5,mockRequestListener);
        consentNetworkError.setErrorCode(2);
        consentNetworkError.setCustomErrorMessage("server","error");
        Assert.assertNotNull(consentNetworkError.getMessage());
    }

    @Test
    public void tesGetStatusCode() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError,3,mockRequestListener);
        consentNetworkError.initMessage(5,mockRequestListener);
        consentNetworkError.setErrorCode(3);
        consentNetworkError.setCustomErrorMessage("server","error");
        Assert.assertEquals(3,consentNetworkError.getStatusCode());
    }

    @Test
    public void testSuccessCode() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError,3,mockRequestListener);
        consentNetworkError.initMessage(5,mockRequestListener);
        consentNetworkError.setErrorCode(CatkConstants.CONSENT_SUCCESS);
        consentNetworkError.setCustomErrorMessage("server","error");
        Assert.assertEquals(CatkConstants.CONSENT_SUCCESS,consentNetworkError.getStatusCode());
    }

    @Test
    public void testNoConnectionErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockNoConnectionError,3,mockRequestListener);
        consentNetworkError.initMessage(2,mockRequestListener);
    }

    @Test
    public void testServerErrorErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockServerError,3,mockRequestListener);
        consentNetworkError.initMessage(3,mockRequestListener);
    }


    @Test
    public void testAuthFailureErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockAuthFailureError,3,mockRequestListener);
        consentNetworkError.initMessage(4,mockRequestListener);
    }

    @Test
    public void testTimeOutErrorType() throws Exception {
        consentNetworkError = new ConsentNetworkError(mockTimeoutError,3,mockRequestListener);
        consentNetworkError.initMessage(5,mockRequestListener);
    }
}