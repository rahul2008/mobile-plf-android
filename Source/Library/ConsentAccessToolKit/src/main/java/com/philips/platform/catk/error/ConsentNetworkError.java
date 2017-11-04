/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catk.error;

import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.catk.listener.NetworkErrorListener;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.CatkConstants;

/**
 * Created by Maqsood on 10/12/17.
 */

public class ConsentNetworkError implements NetworkErrorListener {

    private ServerError mServerError;
    private VolleyError mVolleyError;
    private int mErrorCode = CatkConstants.CONSENT_SUCCESS;
    private String mCustomErrorMessage;
    private String mServerName;

    public ConsentNetworkError(VolleyError error, int requestCode,
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
            requestListener.onResponseError(msg);
        }
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

    public void setCustomErrorMessage(String server, String errorMessage) {
        mServerName = server;
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
            Log.e("Network error",e.getMessage());
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
