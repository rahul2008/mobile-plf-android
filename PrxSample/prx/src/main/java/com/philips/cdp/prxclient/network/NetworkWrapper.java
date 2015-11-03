package com.philips.cdp.prxclient.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This is the Network Wrapper class.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();


    private Context mContext = null;

    public NetworkWrapper(Context context ) {
        mContext = context;
    }



    public void executeJsonObjectRequest(final PrxDataBuilder prxDataBuilder, final ResponseListener listener) {


        RequestQueue mVolleyRequest = Volley.newRequestQueue(mContext);

        Log.d(TAG, "Url : " + prxDataBuilder.getRequestUrl());
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(0, prxDataBuilder.getRequestUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                ResponseData responseData = prxDataBuilder.getResponseData(response);
                listener.onResponseSuccess(responseData);

                Log.d(TAG, "Response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onResponseError(error.toString());
            }
        });
        mVolleyRequest.add(mJsonObjectRequest);
    }
}
