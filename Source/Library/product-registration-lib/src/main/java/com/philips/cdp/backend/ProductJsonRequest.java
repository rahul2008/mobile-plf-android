package com.philips.cdp.backend;

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

public class ProductJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> mResponseListener;
    private ErrorListener mErrorListener;
    private Map<String, String> params;

    public ProductJsonRequest(int method, String url, Map<String, String> params,
                              Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        mErrorListener = errorListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
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
        mResponseListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
}