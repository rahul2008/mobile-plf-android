/*  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.connectivity.models.Measurement;
import com.philips.platform.appframework.connectivity.models.MomentDetail;
import com.philips.platform.appframework.connectivity.models.UserMoment;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.philips.platform.baseapp.screens.utility.Constants.SERVER_DATACREATION;
import static com.philips.platform.baseapp.screens.utility.Constants.SERVER_DATAPARSING;


public class PostMomentRquest extends PlatformRequest {
    public static final String TAG = PostMomentRquest.class.getSimpleName();

    private UserMoment userMoment;

    private PostMomentResponseListener postMomentResponseListener;

    private User user;

    private String baseUrl;

    public interface PostMomentResponseListener {
        void onPostMomentSuccess(String momentId);

        void onPostMomentError(VolleyError error);
    }

    public PostMomentRquest(final UserMoment userMoment, String baseUrl,User user, final PostMomentResponseListener postMomentResponseListener) {
        this.userMoment = userMoment;
        this.postMomentResponseListener = postMomentResponseListener;
        this.baseUrl=baseUrl;
        this.user = user;
    }

    @Override
    public void executeRequest(Context context) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (getRequestType(), getUrl(), getParams(), getResponseListener(), getErrorResponseListener()) {

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
        return Request.Method.POST;
    }

    //TODO:Need to remove this. DOnt use hard coded url. Check with Deepthi.
    @Override
    public String getUrl() {
            return baseUrl + "/api/users/" + user.getHsdpUUID() + "/moments";
    }

    public JSONObject getParams() {
        try {
            JSONObject parent = new JSONObject();
            JSONArray detailsJsonArray = new JSONArray();
            for (MomentDetail momentDetail : userMoment.getMomentDetailArrayList()) {
                JSONObject detailsJsonObj1 = new JSONObject();
                detailsJsonObj1.put(MomentDetail.TYPE, momentDetail.getType());
                detailsJsonObj1.put(MomentDetail.VALUE, momentDetail.getValue());
                detailsJsonArray.put(detailsJsonObj1);
            }
            parent.put(UserMoment.DETAILS, detailsJsonArray);

            JSONArray measurementsJsonArray = new JSONArray();
            for (Measurement measurement : userMoment.getMeasurementArrayList()) {
                JSONObject measurementJsonObj1 = new JSONObject();
                JSONArray detailsMJsonArray = new JSONArray();
                for (MomentDetail momentDetail : measurement.getMomentDetailArrayList()) {
                    JSONObject detailsMJsonObj1 = new JSONObject();
                    detailsMJsonObj1.put(MomentDetail.TYPE, momentDetail.getType());
                    detailsMJsonObj1.put(MomentDetail.VALUE, momentDetail.getValue());
                    detailsMJsonArray.put(detailsMJsonObj1);
                }
                measurementJsonObj1.put(Measurement.DETAILS, detailsMJsonArray);
                measurementJsonObj1.put(Measurement.TIME_STAMP, "2015-08-14T07:07:14.000Z");
                measurementJsonObj1.put(Measurement.TYPE, "Duration");
                measurementJsonObj1.put(Measurement.UNIT, "sec");
                measurementJsonObj1.put(Measurement.VALUE, "100");
                measurementsJsonArray.put(measurementJsonObj1);
            }
            parent.put(UserMoment.MEASUREMENTS, measurementsJsonArray);

            parent.put(UserMoment.TIME_STAMP, "2015-08-13T14:54:25+0200");
            parent.put(UserMoment.TYPE, "Example");
            return parent;
        } catch (JSONException e) {
            RALog.e(TAG+ SERVER_DATAPARSING,
                    e.getMessage());            return null;
        }
    }

    @Override
    public Response.Listener<JSONObject> getResponseListener() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    postMomentResponseListener.onPostMomentSuccess(response.getString("momentId"));
                } catch (JSONException e) {
                    AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, SERVER_DATACREATION,
                            e.getMessage());                }
            }
        };
    }

    @Override
    public Response.ErrorListener getErrorResponseListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                postMomentResponseListener.onPostMomentError(error);
            }
        };
    }
}
