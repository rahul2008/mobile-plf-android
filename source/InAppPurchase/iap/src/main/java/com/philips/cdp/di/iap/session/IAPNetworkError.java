package com.philips.cdp.di.iap.session;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.error.ServerError;

public class IAPNetworkError implements IAPNetworkErrorListener {

    ServerError mServerError = null;
    VolleyError mVolleyError = null;

    public IAPNetworkError(VolleyError error) {
        mVolleyError = error;
        setServerError(mVolleyError);
    }

    @Override
    public String getMessage() {
        if (mServerError != null) {
            return mServerError.getErrors().get(0).getMessage();
        }else{
            return mVolleyError.getMessage();
        }
    }

    @Override
    public int getStatusCode() {
        if (mVolleyError.networkResponse != null)
            return mVolleyError.networkResponse.statusCode;
        return 0;
    }

    private void setServerError(VolleyError error) {
        if (error.networkResponse != null) {
            String errorString = new String(error.networkResponse.data);
            mServerError = new Gson().fromJson(errorString, ServerError.class);
        }
    }
}
