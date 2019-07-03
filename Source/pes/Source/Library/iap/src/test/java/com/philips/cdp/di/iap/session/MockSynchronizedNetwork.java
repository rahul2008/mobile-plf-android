package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockSynchronizedNetwork extends SynchronizedNetwork {
    private VolleyError mVolleyError;
    private String mResponseObject;

    public MockSynchronizedNetwork(final HurlStack stack) {
        super(stack);
    }

    public void setResponse(String object) {
        mResponseObject = object;
    }

    public void setVolleyError(VolleyError error) {
        mVolleyError = error;
    }

    @Override
    public void performRequest(final IAPJsonRequest request, final SynchronizedNetworkListener callBack) {
        if (mResponseObject != null && callBack != null) {
            try {
                HashMap<String,String> params = new HashMap<>();
                if(request.getParams() != null) {
                }
                Response<JSONObject> mockResult = request.parseNetworkResponse(new NetworkResponse(200,
                        mResponseObject.getBytes(), params, false, 0));
            callBack.onSyncRequestSuccess(mockResult);
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
        } else {
            callBack.onSyncRequestError(mVolleyError);
        }
    }
}
