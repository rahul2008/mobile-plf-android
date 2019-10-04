/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.session;

import android.os.Message;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.mec.analytics.MECAnalytics;
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant;
import com.philips.cdp.di.mec.response.error.ServerError;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.MECLog;

public class MECNetworkError implements MECNetworkErrorListener {

    private ServerError mServerError;
    private VolleyError mVolleyError;
    private int mMECErrorCode = MECConstant.MEC_SUCCESS;
    private String mCustomErrorMessage;
    private String mServerName;

    public MECNetworkError(VolleyError error, int requestCode,
                           RequestListener requestListener) {
        initErrorCode(error);
        if (error instanceof com.android.volley.ServerError) {
            setServerError(error);
        } else {
            mVolleyError = error;
        }


        initMessage(requestCode, requestListener);

    }

    void initMessage(int requestCode, RequestListener requestListener) {
        Message msg = Message.obtain();
        msg.what = requestCode;
        msg.obj = this;
        if (requestListener != null) {
            requestListener.onError(msg);
        }
    }

    private void initErrorCode(final VolleyError error) {
        if (error instanceof NoConnectionError) {
            mMECErrorCode = MECConstant.MEC_ERROR_NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            mMECErrorCode = MECConstant.MEC_ERROR_AUTHENTICATION_FAILURE;
        } else if (error instanceof TimeoutError) {
            mMECErrorCode = MECConstant.MEC_ERROR_CONNECTION_TIME_OUT;
        } else if (error instanceof com.android.volley.ServerError) {
            mMECErrorCode = MECConstant.MEC_ERROR_SERVER_ERROR;
        } else {
            mMECErrorCode = MECConstant.MEC_ERROR_UNKNOWN;
        }
    }

    @Override
    public String getMessage() {
        if (mCustomErrorMessage != null) {
            return mCustomErrorMessage;
        } else if (mServerError != null) {
            if (mServerError.getErrors() == null || mServerError.getErrors().get(0) == null) {
                return null;
            }
            return mServerError.getErrors().get(0).getMessage();
        } else if (mVolleyError != null) {
            if (mVolleyError.getMessage() != null) {
                MECAnalytics.trackAction(MECAnalyticsConstant.SEND_DATA,
                        MECAnalyticsConstant.ERROR, MECAnalyticsConstant.SERVER + mMECErrorCode + "_" + mVolleyError.getMessage());
            }
            return mVolleyError.getMessage();
        }
        return null;
    }

    public void setCustomErrorMessage(String server, String errorMessage) {
        mServerName = server;
        mCustomErrorMessage = errorMessage;
    }

    @Override
    public int getStatusCode() {
        if (mVolleyError != null && mVolleyError.networkResponse != null) {
            return mVolleyError.networkResponse.statusCode;
        }
        return mMECErrorCode;
    }

    public void setmMECErrorCode(int mMECErrorCode) {
        this.mMECErrorCode = mMECErrorCode;
    }

    public int getIAPErrorCode() {
        return mMECErrorCode;
    }

    private void setServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
                final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
                final String errorString = new String(decode);

                mServerError = new Gson().fromJson(errorString, ServerError.class);
                if (getMessage() != null) {
                    MECAnalytics.trackAction(MECAnalyticsConstant.SEND_DATA,
                            MECAnalyticsConstant.ERROR, MECAnalyticsConstant.HYBRIS + getMessage());
                }
                checkInsufficientStockError(mServerError);
            }
        } catch (Exception e) {
            MECLog.e(e.getMessage(), MECNetworkError.class.getName());
        }
    }

    private void checkInsufficientStockError(ServerError serverError) {
        if (serverError == null || serverError.getErrors() == null
                || serverError.getErrors().get(0) == null) {
            return;
        }
        if ("InsufficientStockError".equals(serverError.getErrors().get(0).getType())) {
            mMECErrorCode = MECConstant.MEC_ERROR_INSUFFICIENT_STOCK_ERROR;
        }
    }

    public ServerError getServerError() {
        return mServerError;
    }
}
