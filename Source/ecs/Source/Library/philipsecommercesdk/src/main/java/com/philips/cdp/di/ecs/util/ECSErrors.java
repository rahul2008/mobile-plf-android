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
        Exception exception = null;
        String message = null;
            if (volleyError instanceof NetworkError || volleyError instanceof NoConnectionError ) {
            message = ECS_CANNOT_CONNECT_INTERNET;
        }  else if (volleyError instanceof AuthFailureError) {
            message = ECS_AUTH_FAILURE_ERROR;
        } else if (volleyError instanceof ServerError) {
            message = ECS_SERVER_NOT_FOUND;
        }  else if (volleyError instanceof ParseError) {
            message = ECS_PARSE_ERROR;
        }else if (volleyError instanceof TimeoutError) {
            message = ECS_CONNECTION_TIMEOUT;
        } else{
            message = ECS_UNKNOWN_ERROR;
        }
        exception = new Exception(message);
        return  exception;
    }

    public static String getDetailErrorMessage(VolleyError volleyError){
        String message =null;
        try {
            String responseBody = new String(volleyError.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            message  = jsonMessage.getString("message");

        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        } catch (Exception e){

        }
    return message;
    }

    public static void showECSToast(Context context, String message){
        try {
            Toast.makeText(context, message,
                    Toast.LENGTH_LONG).show();
        }catch(Exception e){

        }
    }
}
