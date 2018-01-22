package com.philips.cdp.registration.restclient;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.rest.TokenProviderInterface;
import com.philips.platform.appinfra.rest.request.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philips on 22/01/18.
 */

public class URRestClientStringRequest extends StringRequest {
    public static final String TAG = URRestClientStringRequest.class
            .getSimpleName();
    private static int DEFAULT_TIMEOUT_MS = 3000;//30 SECONDS
    private final String mBody;
    private Response.Listener<String> mResponseListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> params;
    private Handler mHandler;


    public URRestClientStringRequest(int method, String url, String body, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> header, Map<String, String> params, TokenProviderInterface tokenProviderInterface) {
        super(method, url, listener, errorListener, header, params, tokenProviderInterface);
        mBody = body;
        mResponseListener = listener;
        mErrorListener = errorListener;
        mHandler = new Handler(Looper.getMainLooper());
        this.params = params;
    }

    @Override
    public Request<?> setRetryPolicy(final RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                URRestClientStringRequest.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cache-control", "no-cache");
        params.put("content-type", "application/x-www-form-urlencoded");

        return params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody.getBytes();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String jsonString = null;
        RLog.d(TAG, "Response = " + response.statusCode);
        RLog.d(TAG, "Response = " + response.data.toString());
        try {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            RLog.e(TAG, "UnsupportedEncodingException : " + e.getMessage());
        }

        return Response.success(jsonString, getCacheEntry());

    }

    @Override
    protected void deliverResponse(String response) {
        RLog.d(TAG, "Response deliverResponse= " + response);
        // urEventing.post(new MobileVerifyCodeResponseEvent(response));
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        postErrorResponseOnUIThread(error);
    }


    public Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    private void postSuccessResponseOnUIThread(final String jsonObject) {
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
