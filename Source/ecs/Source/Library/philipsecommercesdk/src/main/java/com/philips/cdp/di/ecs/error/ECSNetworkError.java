/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.error;

import android.os.Message;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.util.ECSErrorReason;


public class ECSNetworkError {


    public ECSErrorEnum getErrorLocalizedErrorMessage(VolleyError volleyError, ServerError mServerError) {
        return getEcsErrorEnum(volleyError,mServerError);
    }

    public ECSErrorEnum getErrorLocalizedErrorMessage(VolleyError volleyError) {
        return getEcsErrorEnum(volleyError,null);
    }

    private ECSErrorEnum getEcsErrorEnum(VolleyError volleyError,ServerError mServerError) {

        String errorType = null;
        ECSErrorEnum errorEnumFromType = ECSErrorEnum.UNKNOWN_ERROR;
        if (volleyError instanceof com.android.volley.ServerError) {
            ServerError serverError = getServerError(volleyError);
            if (serverError.getErrors() != null && serverError.getErrors().size() != 0 && serverError.getErrors().get(0).getType() != null) {
                errorType = serverError.getErrors().get(0).getType();

                errorEnumFromType = ECSErrorEnum.getErrorEnumFromType(errorType);
                if(mServerError!=null) {
                    mServerError = serverError;
                }
            }
        } else {

            errorType = getVolleyErrorType(volleyError);
            errorEnumFromType = ECSErrorEnum.getErrorEnumFromType(errorType);
        }
        return errorEnumFromType;
    }


    private String getVolleyErrorType(final VolleyError error) {
        String errorType = ECSErrorReason.ECS_UNKNOWN_ERROR;
        if (error instanceof NoConnectionError || error instanceof NetworkError) {

            errorType = ECSErrorReason.ECS_CANNOT_CONNECT_INTERNET;
        } else if (error instanceof AuthFailureError) {

        } else if (error instanceof TimeoutError) {

        } else if (error instanceof com.android.volley.ServerError) {

        }

        return errorType;
    }

    private ServerError getServerError(VolleyError error) {
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
