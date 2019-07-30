package com.philips.cdp.di.ecs.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_AUTH_FAILURE_ERROR;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_CANNOT_CONNECT_INTERNET;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_CONNECTION_TIMEOUT;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_PARSE_ERROR;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_SERVER_NOT_FOUND;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_UNKNOWN_ERROR;

public class ECSErrors {


    public static Exception getErrorMessage(VolleyError volleyError) {
        String errorType = ECS_UNKNOWN_ERROR;
        if (volleyError instanceof NetworkError || volleyError instanceof NoConnectionError) {
            errorType = ECS_CANNOT_CONNECT_INTERNET;
        } else if (volleyError instanceof AuthFailureError) {
            errorType=ECS_AUTH_FAILURE_ERROR;
            try {
                JSONObject jsonError = new JSONObject(getDetailErrorMessage(volleyError));
                String type = jsonError.getString("type");
                if (null != type && !type.isEmpty()) {
                    errorType = type;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (volleyError instanceof ServerError) {
            errorType = ECS_SERVER_NOT_FOUND;
        } else if (volleyError instanceof ParseError) {
            errorType = ECS_PARSE_ERROR;
        } else if (volleyError instanceof TimeoutError) {
            errorType = ECS_CONNECTION_TIMEOUT;
        }
        Exception exception = new Exception(errorType);
        return exception;
    }

    public static String getDetailErrorMessage(VolleyError volleyError) {
        String message = null;
        try {
            String responseBody = new String(volleyError.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);

            message = jsonMessage.toString();
            //jsonMessage.getString("message");

        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        } catch (Exception e) {

        }
        return message;
    }

    public static void showECSToast(Context context, String message) {
        try {
            Toast.makeText(context, message,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }
}
