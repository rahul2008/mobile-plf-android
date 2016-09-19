package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.philips.cdp.registration.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetMomentRequest extends PlatformRequest {

    private String url;

    private GetMomentResponseListener getMomentResponseListener;

    private String momentId;

    private User user;

    public GetMomentRequest(final GetMomentResponseListener getMomentResponseListener, final User user, final String momentId) {
        this.getMomentResponseListener = getMomentResponseListener;
        this.user = user;
        this.momentId = momentId;
    }

    public interface GetMomentResponseListener {
        void onGetMomentSuccess(String momentId);

        void onGetMomentError(VolleyError error);
    }

    @Override
    public void executeRequest(Context context) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (getRequestType(), getUrl(), null, getResponseListener(), getErrorResponseListener()) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("authorization", "bearer " + user.getHsdpAccessToken());
                headers.put("performerid", user.getHsdpUUID());
                headers.put("api-version", "7");
                headers.put("appagent", "PlatformInfra Postman");
                headers.put("cache-control", "no-cache");
                headers.put("postman-token", "ba0eff76-322f-9fa3-effe-97e8b0e09e93");
                return headers;
            }
        };

// Access the RequestQueue through your singleton class.
        RequestManager.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    public int getRequestType() {

        return Request.Method.GET;
    }

    @Override
    public String getUrl() {
        return "https://referenceplatform-ds-platforminfradev.cloud.pcftest.com//api/users/c8ccf342-7a32-4a87-838f-d31d3949ad59/moments/" + momentId;
    }

    @Override
    public Response.Listener<JSONObject> getResponseListener() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String value = null;

                try {

                    JSONArray jsonArray1 = response.getJSONArray("details");

                    JSONObject detailsValue = jsonArray1.getJSONObject(0);

                    value = detailsValue.getString("value");
                    getMomentResponseListener.onGetMomentSuccess(value);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    @Override
    public Response.ErrorListener getErrorResponseListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                getMomentResponseListener.onGetMomentError(error);
            }
        };
    }
}
