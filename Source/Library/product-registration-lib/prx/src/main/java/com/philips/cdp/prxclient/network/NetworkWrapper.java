package com.philips.cdp.prxclient.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.philips.cdp.prxclient.HttpsTrustManager;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Description : This is the Network Wrapper class.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();


    private Context mContext = null;

    public NetworkWrapper(Context context) {
        mContext = context;
    }


    public void executeJsonObjectRequest(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {


        RequestQueue mVolleyRequest = Volley.newRequestQueue(mContext);

        PrxLogger.d(TAG, "Url : " + prxDataBuilder.getRequestUrl());
//        makeStringRequest(prxDataBuilder.getRequestUrl(),mVolleyRequest);
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
                    try
                    {
                        listener.onResponseError(error.toString(), error.networkResponse.statusCode);
                    }catch (Exception e)
                    {
                        PrxLogger.e(TAG, "Volley Error : " + e);
                        listener.onResponseError(error.toString(), 0);
                    }


                }
            }
        });
        HttpsTrustManager.allowAllSSL();
        mVolleyRequest.add(mJsonObjectRequest);
    }

    private void makeStringRequest(String url, final RequestQueue mVolleyRequest) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response :" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "max");
                params.put("password", "123456");
                return params;
            }
        };
        HttpsTrustManager.allowAllSSL();
        mVolleyRequest.add(strReq);
    }
}
