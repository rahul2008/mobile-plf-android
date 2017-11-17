/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.philips.cdp.registration.User;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class GetMomentRequest extends PlatformRequest {
    public static final String TAG = GetMomentRequest.class.getSimpleName();

    private String url;

    private GetMomentResponseListener getMomentResponseListener;

    private String momentId;

    private User user;

    private String baseUrl;

    public GetMomentRequest(final GetMomentResponseListener getMomentResponseListener,String baseUrl, final User user, final String momentId) {
        this.getMomentResponseListener = getMomentResponseListener;
        this.user = user;
        this.momentId = momentId;
        this.baseUrl=baseUrl;
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
                return getMomentHeader(user);
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
        return baseUrl + "/api/users/" + user.getHsdpUUID() + "/moments/" + momentId;
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
                    RALog.e( TAG,
                            ex.getMessage());                }
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
