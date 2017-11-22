/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.listener.NetworkErrorListener;

public class ConsentNetworkError implements NetworkErrorListener {

    private static final String UNKNOWN_SERVER_ERROR = "Unknown server error";
    private static final String UNKNOWN_VOLLEY_ERROR = "Unknown volley error";
    private static final String UNKNOWN_NETWORK_ERROR = "Unknown network error";
    private ServerError mServerError;
    private VolleyError mVolleyError;
    private int mCatkErrorCode = CatkConstants.CONSENT_SUCCESS;
    private String mCustomErrorMessage;

    public ConsentNetworkError(VolleyError error) {
        initErrorCode(error);
        if (error instanceof com.android.volley.ServerError) {
            setServerError(error);
        } else {
            mVolleyError = error;
        }
    }

    private void initErrorCode(final VolleyError error) {
        if (error instanceof NoConnectionError) {
            mCatkErrorCode = CatkConstants.CONSENT_ERROR_NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            mCatkErrorCode = CatkConstants.CONSENT_ERROR_AUTHENTICATION_FAILURE;
        } else if (error instanceof TimeoutError) {
            mCatkErrorCode = CatkConstants.CONSENT_ERROR_CONNECTION_TIME_OUT;
        } else if (error instanceof com.android.volley.ServerError) {
            mCatkErrorCode = CatkConstants.CONSENT_ERROR_SERVER_ERROR;
        } else {
            mCatkErrorCode = CatkConstants.CONSENT_ERROR_UNKNOWN;
        }
    }

    @Override
    public String getMessage() {
        if (mCustomErrorMessage != null) {
            return mCustomErrorMessage;
        } else if (mServerError != null) {
            if (mServerError.getDescription() != null) {
                return mServerError.getDescription();
            }
            return UNKNOWN_SERVER_ERROR;
        } else if (mVolleyError != null) {
            if (mVolleyError.getMessage() != null) {
                return mVolleyError.getMessage();
            }
            return UNKNOWN_VOLLEY_ERROR;
        }
        return UNKNOWN_NETWORK_ERROR;
    }

    public void setCustomErrorMessage(String errorMessage) {
        mCustomErrorMessage = errorMessage;
    }

    @Override
    public int getStatusCode() {
        if (mVolleyError != null && mVolleyError.networkResponse != null) {
            return mVolleyError.networkResponse.statusCode;
        }
        return mCatkErrorCode;
    }

    public void setCatkErrorCode(int mCatkErrorCode) {
        this.mCatkErrorCode = mCatkErrorCode;
    }

    public int getCatkErrorCode() {
        return mCatkErrorCode;
    }

    public ServerError getServerError() {
        return mServerError;
    }

    private void setServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                String errorString = new String(error.networkResponse.data);
                mServerError = new Gson().fromJson(errorString, ServerError.class);
            }
        } catch (Exception e) {
            Log.e("Network error", e.getMessage());
            mServerError = null;
        }
    }
}
