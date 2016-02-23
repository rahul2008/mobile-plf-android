package com.philips.cdp.prxclient.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.prxclient.HttpsTrustManager;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.json.JSONObject;

/**
 * Description : This is the Network Wrapper class.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();


    private Context mContext = null;
    private RequestQueue mVolleyRequest;
    private boolean isHttpsRequest = true;


    public NetworkWrapper(Context context) {
        mContext = context;
    }


    public void executeJsonObjectRequest(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {
        mVolleyRequest = Volley.newRequestQueue(mContext);
        PrxLogger.d(TAG, "Url : " + prxDataBuilder.getRequestUrl());
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(0, prxDataBuilder.getRequestUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResponseData responseData = prxDataBuilder.getResponseData(response);
                listener.onResponseSuccess(responseData);

                PrxLogger.d(TAG, "Response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    try {
                        listener.onResponseError(error.toString(), error.networkResponse.statusCode);
                    } catch (Exception e) {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                        listener.onResponseError(error.toString(), 0);
                    }


                }
            }
        });
        mVolleyRequest.add(mJsonObjectRequest);
    }

    public void executeCustomRequest(final Request request) {
        mVolleyRequest = Volley.newRequestQueue(mContext);
        if (isHttpsRequest)
            HttpsTrustManager.allowAllSSL();
        mVolleyRequest.add(request);
    }
    
    public void setHttpsRequest(boolean isHttpsRequest) {
        this.isHttpsRequest = isHttpsRequest;
    }
}
