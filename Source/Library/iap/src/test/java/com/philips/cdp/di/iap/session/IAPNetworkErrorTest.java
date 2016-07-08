/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.philips.cdp.di.iap.utils.IAPConstant;

import junit.framework.Assert;

import org.junit.Test;

public class IAPNetworkErrorTest {

    @Test
    public void checkNoConnectionError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new NoConnectionError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getStatusCode());
    }

    @Test
    public void checkAuthFailureError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new AuthFailureError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getStatusCode());
    }

    @Test
    public void checkTimeoutError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new TimeoutError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getStatusCode());
    }

    @Test
    public void checkServerError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new ServerError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }
}
