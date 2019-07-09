/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.ecs.volley;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import java.net.HttpURLConnection;



public class IAPUrlRedirectionHandler {

    public static final int HTTP_REDIRECT = 307;

    IAPJsonRequest mRequest;
    VolleyError volleyError;

    public IAPUrlRedirectionHandler(IAPJsonRequest request, VolleyError error){
        mRequest = request;
        volleyError = error;
    }

    public IAPJsonRequest getNewRequestWithRedirectedUrl() {

        IAPJsonRequest requestWithNewUrl = null;
        try {
            requestWithNewUrl = new IAPJsonRequest(mRequest.getMethod(), getLocation(), mRequest.getParams(),
                    null, mRequest.getErrorListener());
        } catch (AuthFailureError authFailureError) {
            logError(authFailureError);
        }
        return requestWithNewUrl;
    }

    private void logError(AuthFailureError authFailureError) {
        final String message = authFailureError.getMessage();
        if(message!=null) {

        }else {

        }
    }

    protected String getLocation() {
        String location = null;
        if(volleyError!=null && volleyError.networkResponse!=null && volleyError.networkResponse.headers!=null) {
            location = volleyError.networkResponse.headers.get("Location");
        }
        return location;
    }

    public boolean isRedirectionRequired() {
        int status = -1;

        if(volleyError!=null && volleyError.networkResponse!=null) {
            status = volleyError.networkResponse.statusCode;
        }
        return status == HTTP_REDIRECT || HttpURLConnection.HTTP_MOVED_PERM == status ||
                status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER &&
                getLocation() != null && !mRequest.getUrl().equalsIgnoreCase(getLocation());
    }
}
