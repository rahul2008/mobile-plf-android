/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.session;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

public class SynchronizedNetwork {
    protected BasicNetwork mBasicNetwork;

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

    private void retryForUrlRedirection(IAPJsonRequest request, VolleyError error, SynchronizedNetworkListener callBack) {
        IAPUrlRedirectionHandler iapUrlRedirectionHandler = new IAPUrlRedirectionHandler(request, error);
        // Handle 30x
        if (iapUrlRedirectionHandler.isRedirectionRequired()) {
            IAPJsonRequest requestNew = iapUrlRedirectionHandler.getNewRequestWithRedirectedUrl();
            performRequest(requestNew, callBack);
        } else {
            callBack.onSyncRequestError(error);
        }
    }

    protected void successResponse(IAPJsonRequest request, SynchronizedNetworkListener callBack, NetworkResponse response) {
        Response<JSONObject> jsonObjectResponse = request.parseNetworkResponse(response);
        callBack.onSyncRequestSuccess(jsonObjectResponse);
    }
}
