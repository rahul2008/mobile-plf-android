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

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class IAPJsonRequest extends Request<JSONObject> {

    private final static int DEFAULT_TIMEOUT_MS = 30000; //30 SECONDS

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
                DEFAULT_TIMEOUT_MS,
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
            String jsonString = new String(response.data);//,
//                    HttpHeaderParser.parseCharset(response.headers));

            JSONObject result = null;

            if (jsonString.length() > 0)
                result = new JSONObject(jsonString);

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    private Response<JSONObject> parseSuccessResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException exception) {
            return Response.error(new ParseError(exception));
        } catch (JSONException jsonException) {
            return Response.error(new ParseError(jsonException));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        postSuccessResponseOnUIThread(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        handleMiscErrors(error);
//        mErrorListener.onErrorResponse(error);
    }

    private void handleMiscErrors(final VolleyError error) {
        if (error instanceof AuthFailureError) {
            HybrisDelegate.getInstance().getNetworkController().refreshOAuthToken(new RequestListener() {
                @Override
                public void onSuccess(final Message msg) {
                    postSelfAgain();
                }

                @Override
                public void onError(final Message msg) {
                    postVolleyErrorOnUIThread(error);
                }
            });
        } else {
            postVolleyErrorOnUIThread(error);
        }
    }

    private void postSelfAgain() {
        SynchronizedNetwork network = new SynchronizedNetwork(HybrisDelegate.getInstance()
                .getNetworkController().mIAPHurlStack.getHurlStack());
        network.performRequest(this, new SynchronizedNetworkCallBack() {
            @Override
            public void onSyncRequestSuccess(final Response<JSONObject> jsonObjectResponse) {
                postSuccessResponseOnUIThread(jsonObjectResponse.result);
            }

            @Override
            public void onSyncRequestError(final VolleyError volleyError) {
                mErrorListener.onErrorResponse(volleyError);
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

    private void postVolleyErrorOnUIThread(final VolleyError volleyError) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mErrorListener.onErrorResponse(volleyError);
            }
        });
    }
}