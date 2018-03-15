/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class SynchronizedNetwork {
    private BasicNetwork mBasicNetwork;

    public SynchronizedNetwork(HurlStack stack) {
        HurlStack hurlStack = stack;
        if (stack == null) {
            hurlStack = new HurlStack();
        }
        mBasicNetwork = new BasicNetwork(hurlStack);
    }

    public void performRequest(final IAPJsonRequest request, SynchronizedNetworkListener callBack) {
        try {
            NetworkResponse response = mBasicNetwork.performRequest(request);
            successResponse(request, callBack, response);

        } catch (VolleyError volleyError) {
            retryForUrlRedirection(request,volleyError,callBack);
        }
    }

    private void retryForUrlRedirection(IAPJsonRequest request, VolleyError error, SynchronizedNetworkListener callBack){
        try {
            final int status = error.networkResponse.statusCode;
            // Handle 30x
            if(HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                final String location = error.networkResponse.headers.get("Location");
                IAPJsonRequest requestNew = new IAPJsonRequest(request.getMethod(), location, request.getParams(),
                        null, request.getErrorListener());
                performRequest(requestNew,callBack);
            }else {
                callBack.onSyncRequestError(error);
            }
        }catch (Exception e){

        }
    }

    protected void successResponse(IAPJsonRequest request, SynchronizedNetworkListener callBack, NetworkResponse response) {
        Response<JSONObject> jsonObjectResponse = request.parseNetworkResponse(response);
        callBack.onSyncRequestSuccess(jsonObjectResponse);
    }
}
