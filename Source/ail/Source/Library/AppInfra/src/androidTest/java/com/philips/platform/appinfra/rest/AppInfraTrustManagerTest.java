package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.rest.request.RequestQueue;

import org.junit.Assert;

import static java.lang.Thread.sleep;

public class AppInfraTrustManagerTest extends AppInfraInstrumentation{

    private RestInterface mRestInterface = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mRestInterface = mAppInfra.getRestClient();
        assertNotNull(mRestInterface);
    }

    private void waitForResponse() {
        try {
            sleep(750);
        } catch (InterruptedException e) {

        }
    }

    public void testCertificateChainValidationSucceeds(){
        RequestQueue requestQueue = mRestInterface.getRequestQueue();
        String url = "https://www.google.com/";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Assert.fail(error.getMessage());
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener);

        requestQueue.add(request);
        waitForResponse();
    }

    public void testCertificateChainValidationFails() {
        RequestQueue requestQueue = mRestInterface.getRequestQueue();
        String url = "https://untrusted-root.badssl.com/";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Assert.fail("Validation should have failed");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener);

        requestQueue.add(request);
        waitForResponse();
    }
}
