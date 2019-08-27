/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.error;

import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.util.ECSErrorReason;


public class ECSNetworkError {


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

    private static ECSError getEcsErrorEnum(VolleyError volleyError, ServerError mServerError) {

        String errorType = null;
        // ECSErrorEnum errorEnumFromType = ECSErrorEnum.unknown;
        ECSErrorEnum ecsErrorEnum = ECSErrorEnum.unknown;
        if (volleyError instanceof com.android.volley.ServerError) {
            ServerError serverError = getServerError(volleyError);
            if (serverError.getErrors() != null && serverError.getErrors().size() != 0 && serverError.getErrors().get(0).getType() != null) {
                Log.e("DETAIL_ERROR", serverError.getErrors().get(0).toString());
                errorType = serverError.getErrors().get(0).getType();
                ecsErrorEnum = ECSErrorEnum.valueOf(errorType);
                mServerError = serverError;

            }
        } else {
            errorType = getVolleyErrorType(volleyError);
            ecsErrorEnum = ECSErrorEnum.valueOf(errorType);
        }
        ECSError ecsError = new ECSError(ecsErrorEnum.getLocalizedErrorString(), ecsErrorEnum.getErrorCode());
        return ecsError;
    }


    private static String getVolleyErrorType(final VolleyError error) {
        String errorType = ECSErrorReason.ECS_UNKNOWN_ERROR;
        if (error instanceof NoConnectionError || error instanceof NetworkError) {

            errorType = ECSErrorReason.ECS_CANNOT_CONNECT_INTERNET;
        } else if (error instanceof AuthFailureError) {

        } else if (error instanceof TimeoutError) {

        } else if (error instanceof com.android.volley.ServerError) {

        }

        return errorType;
    }

    private static ServerError getServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
                final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
                final String errorString = new String(decode);

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

}
