/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.plataform.mya.model.request;

import android.os.Handler;
import android.os.Looper;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.philips.plataform.mya.model.utils.ConsentUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ConsentRequest extends Request<JsonArray> {

    private Listener<JsonArray> mResponseListener;
    private ErrorListener mErrorListener;
    private Map<String, String> params;
    private Handler mHandler;
    private Map<String, String> header;

    public ConsentRequest(int method, String url,  Map<String, String> header, Map<String, String> params,
                          Listener<JsonArray> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        mErrorListener = errorListener;
        mHandler = new Handler(Looper.getMainLooper());
        this.params = params;
        this.header = header;
    }

    @Override
    public Request<?> setRetryPolicy(final RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                ConsentUtil.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    @Override
    protected Response<JsonArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = (JsonArray)parser.parse(jsonString);

            return Response.success(jsonArray,
                    HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JsonArray response) {
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        postErrorResponseOnUIThread(error);
    }

    private void postSuccessResponseOnUIThread(final JsonArray jsonArray) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseListener.onResponse(jsonArray);
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