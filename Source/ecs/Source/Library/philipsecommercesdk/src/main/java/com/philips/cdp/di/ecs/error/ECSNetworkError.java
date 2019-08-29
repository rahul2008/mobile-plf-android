/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.error;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.R;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.platform.appinfra.logging.LoggingInterface;




public class ECSNetworkError {


    private static final String LOGGING_TAG = "";

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
        Log.e("ON_SUCCESS_ERROR", logMessage);
        ECSConfig.INSTANCE.getAppInfra().getLogging().log(LoggingInterface.LogLevel.ERROR,"ON_SUCCESS_ERROR",logMessage);
        return  new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());

    }

    private static ECSError getEcsErrorEnum(VolleyError volleyError, ServerError mServerError) {

        String errorType = null;
        ECSErrorEnum ecsErrorEnum = ECSErrorEnum.something_went_wrong;
        if (volleyError instanceof com.android.volley.ServerError || volleyError instanceof AuthFailureError) {
            ServerError serverError = getServerError(volleyError);
            if (serverError.getErrors() != null && serverError.getErrors().size() != 0 && serverError.getErrors().get(0).getType() != null) {
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
        ECSError ecsError = new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());
        return ecsError;
    }


    private static ECSErrorEnum getVolleyErrorType(final VolleyError error) {
        if(error.getMessage()!=null) {
            Log.e("ON_VOLLEY_ERROR", error.getMessage());
        }
        ECSConfig.INSTANCE.getAppInfra().getLogging().log(LoggingInterface.LogLevel.ERROR,"ON_VOLLEY_ERROR",error.getMessage());
         ECSErrorEnum ecsErrorEnum = ECSErrorEnum.something_went_wrong;
        if (error instanceof NoConnectionError || error instanceof NetworkError) {
            ecsErrorEnum = ECSErrorEnum.ecs_no_internet;
        }  else if (error instanceof TimeoutError) {
            ecsErrorEnum = ECSErrorEnum.ecs_connection_timeout;
        } else if (error instanceof com.android.volley.ServerError) {
            ecsErrorEnum = ECSErrorEnum.ecs_server_not_found;
        }
        return ecsErrorEnum;
    }

    private static ServerError getServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
                final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
                final String errorString = new String(decode);
                ECSConfig.INSTANCE.getAppInfra().getLogging().log(LoggingInterface.LogLevel.ERROR,LOGGING_TAG,errorString);

                return new Gson().fromJson(errorString, ServerError.class);


                //checkInsufficientStockError(mServerError);
            }
        } catch (Exception e) {


        }
        return null;
    }

    private void checkInsufficientStockError(ServerError serverError) {
        if (serverError == null || serverError.getErrors() == null
                || serverError.getErrors().get(0) == null) {
            return;
        }
        if ("InsufficientStockError".equals(serverError.getErrors().get(0).getType())) {
            // ECSErrorConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR;
        }
    }

    public static void showECSAlertDialog(Context context, String title, String message ){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                //.setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
