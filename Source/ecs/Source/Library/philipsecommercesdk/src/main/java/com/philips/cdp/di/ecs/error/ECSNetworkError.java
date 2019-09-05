/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.error;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.logging.LoggingInterface;


public class ECSNetworkError {


    private static final String LOGGING_TAG = ECSNetworkError.class.getSimpleName();

    public static ECSError getErrorLocalizedErrorMessage(VolleyError volleyError) {
        ServerError serverError = new ServerError();
        return getEcsErrorEnum(volleyError, serverError);
    }

    public static ECSError getErrorLocalizedErrorMessageForAddress(VolleyError volleyError) {
        ServerError serverError = new ServerError();
        ECSError ecsError = getEcsErrorEnum(volleyError, serverError);
        if (null != serverError.getErrors() && serverError.getErrors().size() > 0 && null != serverError.getErrors().get(0).getSubject()) {
            String errorType = serverError.getErrors().get(0).getSubject();
            ECSErrorEnum ecsErrorEnum = ECSErrorEnum.valueOf(errorType);

            ecsError = new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());
        }
        return ecsError;
    }

    public  static ECSError getErrorLocalizedErrorMessage(ECSErrorEnum ecsErrorEnum,Exception exception, String hysbrisResponse){
        String logMessage=ecsErrorEnum.toString();
        if(null!=exception){
            logMessage="\n\n"+exception.getLocalizedMessage();
        }
        if(null!=hysbrisResponse){
            logMessage="\n\n"+hysbrisResponse;
        }
        Log.e(LOGGING_TAG, logMessage);
        try {
            ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG+"getErrorLocalizedErrorMessage", logMessage);
        }catch(Exception e){

        }
        return  new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());

    }

    private static ECSError getEcsErrorEnum(VolleyError volleyError, ServerError mServerError) {

        String errorType = null;
        ECSErrorEnum ecsErrorEnum = ECSErrorEnum.somethingWentWrong;
        if (volleyError instanceof com.android.volley.ServerError || volleyError instanceof AuthFailureError) {
            ServerError serverError = getServerError(volleyError);
            if (serverError!=null && serverError.getErrors() != null && serverError.getErrors().size() != 0 && serverError.getErrors().get(0).getType() != null) {
                Log.e("ON_FAILURE_ERROR", serverError.getErrors().get(0).toString());
                errorType = serverError.getErrors().get(0).getType();
                mServerError.setErrors(serverError.getErrors());
                ecsErrorEnum = ECSErrorEnum.valueOf(errorType);
            }else  if(volleyError instanceof AuthFailureError) { // If it is AuthFailureError other than InvalidHybris Token
                ecsErrorEnum = ECSErrorEnum.ecs_volley_auth_error;
            }
        } else {
            ecsErrorEnum = getVolleyErrorType(volleyError);
        }
        return new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());

    }


    private static ECSErrorEnum getVolleyErrorType(final VolleyError error) {
        if(error.getMessage()!=null) {
            Log.e(LOGGING_TAG + " Volley Error: ", error.getMessage());

            try {
                ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG + " Volley Error: ", error.getMessage());
            } catch (Exception e) {

            }
        }
         ECSErrorEnum ecsErrorEnum = ECSErrorEnum.somethingWentWrong;
        if (error instanceof NoConnectionError || error instanceof NetworkError) {
            ecsErrorEnum = ECSErrorEnum.ecs_no_internet;
        }  else if (error instanceof TimeoutError) {
            ecsErrorEnum = ECSErrorEnum.ecs_connection_timeout;
        }
        return ecsErrorEnum;
    }

    private static ServerError getServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                System.out.println("print base64 byte"+error.networkResponse.data);
                final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
                return parseServerError(encodedString);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static ServerError parseServerError(String encodedString) {
        final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
        final String errorString = new String(decode);
        try {
            ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG, errorString);
        }catch (Exception e){

        }
        return new Gson().fromJson(errorString, ServerError.class);
    }

}
