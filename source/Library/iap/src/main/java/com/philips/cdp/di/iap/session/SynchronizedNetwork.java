/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

public class SynchronizedNetwork {
    private BasicNetwork mBasicNetwork;

    public SynchronizedNetwork(HurlStack stack) {
        HurlStack hurlStack = stack;
        if (stack == null) {
            hurlStack = new HurlStack();
        }
        mBasicNetwork = new BasicNetwork(hurlStack);
    }

    public void performRequest(IAPJsonRequest request, SynchronizedNetworkCallBack callBack) {
        try {
            NetworkResponse response = mBasicNetwork.performRequest(request);
            successResponse(request, callBack, response);

        } catch (VolleyError volleyError) {
            callBack.onSyncRequestError(volleyError);
        }
    }

    protected void successResponse(IAPJsonRequest request, SynchronizedNetworkCallBack callBack, NetworkResponse response) {
        Response<JSONObject> jsonObjectResponse = request.parseNetworkResponse(response);
        callBack.onSyncRequestSuccess(jsonObjectResponse);
    }
}
