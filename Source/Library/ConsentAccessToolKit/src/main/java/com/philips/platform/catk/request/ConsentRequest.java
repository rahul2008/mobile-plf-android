/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catk.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.listener.ConsentRequestListener;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ConsentRequest extends Request<JsonArray> {
    private ConsentRequestListener mResponseListener;
    private String body;
    private Map<String, String> header;

    private static final int POST_SUCCESS_CODE = 201;

    public ConsentRequest(int method, String url, Map<String, String> header, String params, ConsentRequestListener responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        this.body = params;
        this.header = header;
    }

    @Override
    public Request<?> setRetryPolicy(final RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                CatkConstants.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body.getBytes();
    }

    @Override
    public Response<JsonArray> parseNetworkResponse(NetworkResponse response) {
        try {
            if (response.statusCode == POST_SUCCESS_CODE) {
                JsonArray postResultArray = new JsonArray();
                postResultArray.add(POST_SUCCESS_CODE);
                return Response.success(postResultArray,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = (JsonArray) parser.parse(jsonString);

            return Response.success(jsonArray,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            ParseError error = new ParseError(e);
            mResponseListener.onErrorResponse(this, error);
            return Response.error(error);
        }
    }

    @Override
    protected void deliverResponse(JsonArray response) {
        mResponseListener.onResponse(this, response);
    }

    @Override
    public void deliverError(final VolleyError error) {
        mResponseListener.onErrorResponse(this, error);
    }
}