package com.cdp.prx.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import horizontal.cdp.prxcomponent.PrxDataBuilder;
import horizontal.cdp.prxcomponent.ResponseData;
import horizontal.cdp.prxcomponent.listeners.ResponseListener;

/**
 * Description : This is the Network Wrapper class.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();


    private PrxDataBuilder mPrxDataBuilder = null;
    private ResponseListener mListener = null;
    private Context mContext = null;
    private ResponseData mResponseData = null;

    public NetworkWrapper(Context context, PrxDataBuilder prxDataBuilder, ResponseListener listener) {
        mContext = context;
        mPrxDataBuilder = prxDataBuilder;
        mListener = listener;
    }

    public void executeJsonObjectRequest() {

        RequestQueue mVolleyRequest = Volley.newRequestQueue(mContext);

        Log.d(TAG, "Url : " + mPrxDataBuilder.getRequestUrl());
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(0, mPrxDataBuilder.getRequestUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

             //   mPrxDataBuilder.set
                mResponseData = new ResponseData(response, mPrxDataBuilder, mListener);
                mResponseData.init();
                Log.d(TAG, "Response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.onResponseError(error.toString());
            }
        });
        mVolleyRequest.add(mJsonObjectRequest);
    }
}
