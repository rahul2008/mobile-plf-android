/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant.PRX;
import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class IAPNetworkErrorTest {

    @Mock
    JSONObject mJsonObject;

    @Test
    public void checkNoConnectionError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new NoConnectionError(), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getIAPErrorCode());
        assertEquals(IAPConstant.IAP_ERROR_NO_CONNECTION, error.getStatusCode());
    }

    @Test
    public void checkAuthFailureError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new AuthFailureError(), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getIAPErrorCode());
        assertEquals(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE, error.getStatusCode());
    }

    @Test
    public void checkTimeoutError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new TimeoutError(), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getIAPErrorCode());
        assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, error.getStatusCode());
    }

    @Test
    public void checkServerError() {
        MockIAPNetworkError error = new MockIAPNetworkError(new ServerError(), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getIAPErrorCode());
        assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void testStatusCode() throws Exception {
        mJsonObject = new JSONObject(TestUtils.readFile(IAPJsonRequestTest.class, "server_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(5, null, null, false);
        MockIAPNetworkError error = new MockIAPNetworkError(new VolleyError(networkResponse), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void testSetServerErrorException() throws Exception {
        mJsonObject = new JSONObject(TestUtils.readFile(IAPJsonRequestTest.class, "server_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(mJsonObject.toString().getBytes("utf-8"));
        MockIAPNetworkError error = new MockIAPNetworkError(new ServerError(networkResponse), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_SERVER_ERROR, error.getStatusCode());
    }

    @Test
    public void testSetServerError() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\insufficient_server_error.txt"));
        new MockIAPNetworkError(new ServerError(networkResponse), 0, null);
    }

    @Test
    public void testSetServerErrorWithNull() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\empty_server_error.txt"));
        new MockIAPNetworkError(new ServerError(networkResponse), 0, null);
    }

    @Test
    public void testGetMessageWithNull() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\empty_server_error.txt"));
        MockIAPNetworkError error = new MockIAPNetworkError(new ServerError(networkResponse), 0, null);
        error.getMessage();
    }

    @Test
    public void testSetCustomErrorMessage(){
        MockIAPNetworkError error = new MockIAPNetworkError(new TimeoutError(), 0, null);
        error.setCustomErrorMessage(PRX,"Custom message");
        assertEquals(error.getMessage(), "Custom message");
    }

    private byte[] fileToByteArray(String path) {
        File file = new File(path);
        System.out.println("Current abs dir: " + file.getAbsolutePath());
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead;
            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
        return byteArray;
    }
}
