package com.philips.cdp.di.iap.session;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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
    private Handler mMainHandler;

    public IAPJsonRequest(int method, String url, Map<String, String> params,
                          Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        mErrorListener = errorListener;
        mMainHandler = new Handler(Looper.getMainLooper());
        this.params = params;
    }

    @Override
    public Request<?> setRetryPolicy(final RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConstants.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data);
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
        handleMiscErrors(error);
    }

    private void handleMiscErrors(final VolleyError error) {
        if (error instanceof AuthFailureError) {
            HybrisDelegate.getNetworkController().refreshOAuthToken(new RequestListener() {
                @Override
                public void onSuccess(final Message msg) {
                    postSelfAgain();
                }

                @Override
                public void onError(final Message msg) {
                    postErrorResponseOnUIThread(error);
                }
            });
        } else {
            postErrorResponseOnUIThread(error);
        }
    }

    private void postSelfAgain() {
        SynchronizedNetwork synchronizedNetwork = new SynchronizedNetwork
                (HybrisDelegate.getNetworkController().mIapHurlStack);
        synchronizedNetwork.performRequest(this, new SynchronizedNetworkListener() {
            @Override
            public void onSyncRequestSuccess(final Response<JSONObject> jsonObjectResponse) {
                postSuccessResponseOnUIThread(jsonObjectResponse.result);
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                postErrorResponseOnUIThread(volleyError);
            }
        });
    }

    private void postSuccessResponseOnUIThread(final JSONObject jsonObject) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseListener.onResponse(jsonObject);
            }
        });
    }

    private void postErrorResponseOnUIThread(final VolleyError volleyError) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mErrorListener.onErrorResponse(volleyError);
            }
        });
    }
}