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


    public URRestClientStringRequest(int method, String url, String body, Response.Listener<String> successListener, Response.ErrorListener errorListener, Map<String, String> header, Map<String, String> params, TokenProviderInterface tokenProviderInterface) {
        super(method, url, successListener, errorListener, header, params, tokenProviderInterface);

        mBody = body;
        mResponseListener = successListener;
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
      //  params.put("Content-Type", "application/json; charset=UTF-8");

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
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        return volleyError;
    }

    @Override
    protected void deliverResponse(String response) {
        RLog.d(TAG, "Response deliverResponse= " + response);
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (error.networkResponse != null) {
            RLog.d(TAG, "deliverError Response error= " + error);
            String body;
            //get status code here
            String statusCode = String.valueOf(error.networkResponse.statusCode);
            RLog.d(TAG, "deliverError Response statusCode= " + statusCode);
            //get response body and parse with appropriate encoding
            if (error.networkResponse.data != null) {
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    RLog.d(TAG, "deliverError Response body= " + body);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        postErrorResponseOnUIThread(error);
    }


    public Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    private void postSuccessResponseOnUIThread(final String jsonObject) {
        RLog.d(TAG, jsonObject);
        mHandler.post(() -> {
            mResponseListener.onResponse(jsonObject);
        });
    }

    private void postErrorResponseOnUIThread(final VolleyError volleyError) {
        // RLog.d(TAG, volleyError.getMessage());
        mHandler.post(() -> mErrorListener.onErrorResponse(volleyError));
    }
}
