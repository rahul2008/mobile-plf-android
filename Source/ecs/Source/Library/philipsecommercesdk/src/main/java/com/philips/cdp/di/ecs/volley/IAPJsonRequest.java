/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.volley;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class IAPJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> mResponseListener;
    private ErrorListener mErrorListener;
    private Map<String, String> params;
    protected Handler mHandler;

    public IAPJsonRequest(int method, String url, Map<String, String> params,
                          Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        mErrorListener = errorListener;
        mHandler = new Handler(Looper.getMainLooper());
        this.params = params;
    }

    @Override
    public Request<?> setRetryPolicy(final RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConstants.DEFAULT_TIMEOUT_MS,
                0,
                0.0f));
    }

    @Override
    public Map<String, String> getParams()
            throws AuthFailureError {
        return params;
    }

    @Override
    public Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            final String encodedString = Base64.encodeToString(response.data, Base64.DEFAULT);
            final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
            final String jsonString = new String(decode);
            JSONObject result = null;
            if (jsonString.length() > 0)
                result = new JSONObject(jsonString);
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(VolleyError error) {

    }



    private void postSuccessResponseOnUIThread(final JSONObject jsonObject) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseListener.onResponse(jsonObject);
            }
        });
    }

    private void postErrorResponseOnUIThread(final VolleyError volleyError) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mErrorListener.onErrorResponse(volleyError);
            }
        });
    }
}