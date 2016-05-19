package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.philips.cdp.di.iap.utils.IAPConstant;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class IAPNetworkErrorTest {

    @Test
    public void checkNoConnectionErrorConstant() {
        IAPNetworkError error = new IAPNetworkError(new NoConnectionError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getStatusCode());

    }

    @Test
    public void checkAuthFailureErrorConstant() {
        IAPNetworkError error = new IAPNetworkError(new AuthFailureError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getStatusCode());
    }

    @Test
    public void checkTimeoutErrorConstant() {
        IAPNetworkError error = new IAPNetworkError(new TimeoutError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getStatusCode());
    }

    @Test
    public void checkServerErrorrConstant() {
        IAPNetworkError error = new IAPNetworkError(new ServerError(), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void checkServerErrorrWithEmptyResponse() {
        NetworkResponse response = new NetworkResponse("".getBytes(), new HashMap<String, String>());
        IAPNetworkError error = new IAPNetworkError(new ServerError(response), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void checkServerErrorrWithHelloResponse() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("result","ok");
        NetworkResponse response = new NetworkResponse(json.toString().getBytes(),
                new HashMap<String,String>());
        IAPNetworkError error = new IAPNetworkError(new ServerError(response), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void checkOutOfStockResponse() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("result", "ok");
        NetworkResponse response = new NetworkResponse(json.toString().getBytes(),
                new HashMap<String, String>());
        IAPNetworkError error = new IAPNetworkError(new ServerError(response), 0, null);
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        Assert.assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }
}
