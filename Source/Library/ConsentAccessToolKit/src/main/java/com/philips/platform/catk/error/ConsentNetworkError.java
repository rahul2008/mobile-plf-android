/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.listener.NetworkErrorListener;

public class ConsentNetworkError implements NetworkErrorListener {

    private ServerError mServerError;
    private VolleyError mVolleyError;
    private int mErrorCode = CatkConstants.CONSENT_SUCCESS;
    private String mCustomErrorMessage;

    public ConsentNetworkError(VolleyError error, int requestCode) {
        initErrorCode(error);
        if (error instanceof com.android.volley.ServerError) {
            setServerError(error);
        } else {
            mVolleyError = error;
        }
        initMessage(requestCode);
    }

    void initMessage(int requestCode) {
        Message msg = Message.obtain();
        msg.what = requestCode;
        msg.obj = this;
    }

    private void initErrorCode(final VolleyError error) {
        if (error instanceof NoConnectionError) {
            mErrorCode = CatkConstants.CONSENT_ERROR_NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            mErrorCode = CatkConstants.CONSENT_ERROR_AUTHENTICATION_FAILURE;
        } else if (error instanceof TimeoutError) {
            mErrorCode = CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT;
        } else if (error instanceof com.android.volley.ServerError) {
            mErrorCode = CatkConstants.CONSENT_ERROR_SERVER_ERROR;
        } else {
            mErrorCode = CatkConstants.CONSENT_ERROR_UNKNOWN;
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
            }
            return mVolleyError.getMessage();
        }
        return null;
    }

    public void setCustomErrorMessage(String errorMessage) {
        mCustomErrorMessage = errorMessage;
    }

    @Override
    public int getStatusCode() {
        if (mVolleyError != null && mVolleyError.networkResponse != null) {
            return mVolleyError.networkResponse.statusCode;
        }
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    private void setServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                String errorString = new String(error.networkResponse.data);
                mServerError = new Gson().fromJson(errorString, ServerError.class);
                checkInsufficientStockError(mServerError);
            }
        } catch (Exception e) {
            Log.e("Network error", e.getMessage());
        }
    }

    private void checkInsufficientStockError(ServerError serverError) {
        if (serverError == null || serverError.getErrors() == null
                || serverError.getErrors().get(0) == null) {
            return;
        }
        if ("InsufficientStockError".equals(serverError.getErrors().get(0).getType())) {
            mErrorCode = CatkConstants.CONSENT_ERROR_INSUFFICIENT_STOCK_ERROR;
        }
    }
}
