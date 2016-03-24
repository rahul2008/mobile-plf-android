package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.error.ServerError;

public class IAPNetworkError implements IAPNetworkErrorListener {

    ServerError mServerError = null;
    VolleyError mVolleyError = null;
    Context mContext;

    public IAPNetworkError(Context context, VolleyError error, int requestCode,
                           RequestListener requestListener) {
        mContext = context;
        if (error instanceof com.android.volley.ServerError) {
            setServerError(error);
        } else {
            mVolleyError = error;
        }
        Message msg = Message.obtain();
        msg.what = requestCode;
        msg.obj = this;
        requestListener.onError(msg);
    }

    @Override
    public String getMessage() {
        if (mServerError != null) {
            return mServerError.getErrors().get(0).getMessage();
        } else if (mVolleyError != null) {
            if (mVolleyError instanceof NoConnectionError) {
                return mContext.getString(R.string.iap_check_connection);
            } else if (mVolleyError instanceof TimeoutError) {
                return mContext.getString(R.string.iap_time_out_error);
            } else {
                return mVolleyError.getMessage();
            }
        } else {
            return mContext.getString(R.string.iap_something_went_wrong);
        }
    }

    @Override
    public int getStatusCode() {
        if (mVolleyError.networkResponse != null)
            return mVolleyError.networkResponse.statusCode;
        return 0;
    }

    private void setServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                String errorString = new String(error.networkResponse.data);
                mServerError = new Gson().fromJson(errorString, ServerError.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerError getServerError() {
        return mServerError;
    }
}
