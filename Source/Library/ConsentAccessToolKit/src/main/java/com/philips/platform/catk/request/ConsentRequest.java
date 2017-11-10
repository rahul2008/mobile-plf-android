/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catk.request;

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
import com.android.volley.toolbox.HurlStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.RefreshTokenListener;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.session.SynchronizedNetwork;
import com.philips.platform.catk.session.SynchronizedNetworkListener;
import com.philips.platform.catk.CatkConstants;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Maqsood on 10/12/17.
 */

public class ConsentRequest extends Request<JsonArray> {

    private Listener<JsonArray> mResponseListener;
    private ErrorListener mErrorListener;
    private String body;
    private Handler mHandler;
    private Map<String, String> header;
    private static final int POST_SUCCESS_CODE = 201;

    public ConsentRequest(int method, String url, Map<String, String> header, String params,
                          Listener<JsonArray> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        mErrorListener = errorListener;
        mHandler = new Handler(Looper.getMainLooper());
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
            postErrorResponseOnUIThread(new ParseError(e));
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            postErrorResponseOnUIThread(new ParseError(e));
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JsonArray response) {
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(final VolleyError error) {
        if (error instanceof AuthFailureError) {
            performRefreshToken(error);
        } else {
            postErrorResponseOnUIThread(error);
        }
    }

    private void performRefreshToken(final VolleyError error) {
        NetworkHelper.getInstance().refreshAccessToken(new RefreshTokenListener() {
            @Override
            public void onRefreshSuccess() {
                persistRequestSend();
            }

            @Override
            public void onRefreshFailed(int errCode) {
                postErrorResponseOnUIThread(error);
            }
        });
    }

    private void persistRequestSend() {
        final SynchronizedNetwork synchronizedNetwork = new SynchronizedNetwork(new HurlStack());
        try {
            NetworkAbstractModel.addAuthorization(ConsentRequest.this.getHeaders());
        } catch (AuthFailureError authFailureError) {
            
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedNetwork.performRequest(ConsentRequest.this, new SynchronizedNetworkListener() {
                    @Override
                    public void onSyncRequestSuccess(final Response<JsonArray> jsonObjectResponse) {
                        postSuccessResponseOnUIThread(jsonObjectResponse.result);
                    }

                    @Override
                    public void onSyncRequestError(final VolleyError volleyError) {
                        postErrorResponseOnUIThread(volleyError);
                    }
                });
            }
        }).start();
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