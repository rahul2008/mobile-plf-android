package com.philips.cdp.prxclient;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PrxRequest extends Request<JSONObject> {

    private Listener<JSONObject> mResponseListener;
    private ErrorListener mErrorListener;
    private Map<String, String> params, headers;

    public PrxRequest(final int method, final String url, final Map<String, String> params, final Map<String, String> headers, final ResponseListener listener, final PrxDataBuilder prxDataBuilder) {
        this(method, url, getErrorListener(listener));
        this.params = params;
        this.headers = headers;
        this.mResponseListener = getOnResponseListener(listener, prxDataBuilder);
    }

    private PrxRequest(int method, String url, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mErrorListener = errorListener;
    }

    @NonNull
    private static ErrorListener getErrorListener(final ResponseListener listener) {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (error != null) {
                    final NetworkResponse networkResponse = error.networkResponse;
                    try {
                        if (networkResponse != null)
                            listener.onResponseError(error.toString(), networkResponse.statusCode);
                        else if (error instanceof NoConnectionError) {
                            listener.onResponseError("No internet connection", ErrorType.NO_INTERNET_CONNECTION.getId());
                        } else
                            listener.onResponseError(ErrorType.UNKNOWN.getDescription(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @NonNull
    private Listener<JSONObject> getOnResponseListener(final ResponseListener listener, final PrxDataBuilder prxDataBuilder) {
        return new Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                ResponseData responseData = prxDataBuilder.getResponseData(response);
                listener.onResponseSuccess(responseData);
            }
        };
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        if (params != null)
            return params;

        return super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers != null)
            return headers;

        return super.getHeaders();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            JSONObject result = null;

            if (jsonString != null && jsonString.length() > 0)
                result = new JSONObject(jsonString);

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mResponseListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
}