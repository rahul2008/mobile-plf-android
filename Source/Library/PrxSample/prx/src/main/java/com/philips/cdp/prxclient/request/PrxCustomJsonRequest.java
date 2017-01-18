package com.philips.cdp.prxclient.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PrxCustomJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> mResponseListener;
    private ErrorListener mErrorListener;
    private Map<String, String> params, headers;

    public PrxCustomJsonRequest(final int method, final String url, final Map<String, String> params,
                                final Map<String, String> headers, Listener<JSONObject> responseListener,
                                final ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mErrorListener = errorListener;
        this.params = params;
        this.headers = headers;
        this.mResponseListener = responseListener;
    }

    @Override
    protected Map<String, String> getParams()
            throws AuthFailureError {
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

            if (jsonString.length() > 0)
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