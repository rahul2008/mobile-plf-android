package com.philips.cdp.prxclient.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.PrxCustomJsonRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.json.JSONObject;

/**
 * Description : This is the Network Wrapper class.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class NetworkWrapper {

    private static final String TAG = NetworkWrapper.class.getSimpleName();
    private Context mContext = null;
    private RequestQueue mVolleyRequest;

    public NetworkWrapper(Context context) {
        mContext = context;
        VolleyQueue volleyQueue = VolleyQueue.getInstance();
        mVolleyRequest = volleyQueue.getRequestQueue(mContext);
    }

    public void executeCustomJsonRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        PrxLogger.d(TAG, "Custom JSON Request call..");
        final Response.Listener<JSONObject> responseListener = getVolleyResponseListener(prxRequest, listener);
        final Response.ErrorListener errorListener = getVolleyErrorListener(listener);
        String url = prxRequest.getRequestUrl();
        PrxCustomJsonRequest request = new PrxCustomJsonRequest(prxRequest.getRequestType(), url, prxRequest.getParams(), prxRequest.getHeaders(), responseListener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                prxRequest.getRequestTimeOut(),
                prxRequest.getMaxRetries(),
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(true);
        if (url.startsWith("https") && (url.contains("tst.philips") || url.contains("acc.philips"))){
            SSLCertificateManager.disableAllServerCertificateChecking();
        }
        mVolleyRequest.add(request);
    }




    private Response.ErrorListener getVolleyErrorListener(final ResponseListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (error != null) {
                    final NetworkResponse networkResponse = error.networkResponse;
                    try {
                        if (error instanceof NoConnectionError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getDescription(), PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getId()));
                        } else if (error instanceof TimeoutError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.TIME_OUT.getDescription(), PrxError.PrxErrorType.TIME_OUT.getId()));
                        } else if (error instanceof AuthFailureError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.AUTHENTICATION_FAILURE.getDescription(), PrxError.PrxErrorType.AUTHENTICATION_FAILURE.getId()));
                        } else if (error instanceof NetworkError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.NETWORK_ERROR.getDescription(), PrxError.PrxErrorType.NETWORK_ERROR.getId()));
                        } else if (error instanceof ParseError) {
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.PARSE_ERROR.getDescription(), PrxError.PrxErrorType.PARSE_ERROR.getId()));
                        } else if (networkResponse != null) {
                            listener.onResponseError(new PrxError(networkResponse.toString(), networkResponse.statusCode));
                        } else
                            listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                    } catch (Exception e) {
                        listener.onResponseError(new PrxError(PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getDescription(), PrxError.PrxErrorType.UNKNOWN_EXCEPTION.getId()));
                    }
                }
            }
        };
    }


    private Response.Listener<JSONObject> getVolleyResponseListener(final PrxRequest prxRequest, final ResponseListener listener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                ResponseData responseData = prxRequest.getResponseData(response);
                listener.onResponseSuccess(responseData);
            }
        };
    }
}
