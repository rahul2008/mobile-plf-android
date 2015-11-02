package com.cdp.prx.network;

import android.content.Context;
import android.util.Log;

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
 * Created by 310190678 on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();

    private static final String PRX_REQUEST_URL = "http://%s/product/%s/%s/%s/products/%s.assets";
    private PrxDataBuilder mPrxDataBuilder = null;
    private ResponseListener mListener = null;
    private Context mContext = null;
    private ResponseData mResponseData = null;

    public NetworkWrapper(Context context, PrxDataBuilder prxDataBuilder, ResponseListener listener) {
        mContext = context;
        mPrxDataBuilder = prxDataBuilder;
        mListener = listener;
    }

    private String getUrl() {
        return String.format(PRX_REQUEST_URL, mPrxDataBuilder.getServerInfo(), mPrxDataBuilder.getSectorCode(), mPrxDataBuilder.getLocale(),
                mPrxDataBuilder.getCatalogCode(), mPrxDataBuilder.getCtnCode());
    }

    public void execute() {

        RequestQueue mVolleyRequest = Volley.newRequestQueue(mContext);

        Log.d(TAG, "Url[" + getUrl());
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(0, getUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mListener.onSuccess();
                mResponseData = new ResponseData(response, mPrxDataBuilder);
                mResponseData.init();
                Log.d(TAG, "Response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.onFail();
                Log.d(TAG, "Error : " + error.toString());
            }
        });
        mVolleyRequest.add(mJsonObjectRequest);
    }
}
