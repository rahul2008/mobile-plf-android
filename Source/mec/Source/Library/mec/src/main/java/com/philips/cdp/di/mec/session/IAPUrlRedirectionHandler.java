/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.mec.session;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.mec.utils.MECLog;


import java.net.HttpURLConnection;

import static com.philips.cdp.di.mec.utils.MECConstant.HTTP_REDIRECT;


public class IAPUrlRedirectionHandler {

    MECJsonRequest mRequest;
    VolleyError volleyError;

    public IAPUrlRedirectionHandler(MECJsonRequest request, VolleyError error){
        mRequest = request;
        volleyError = error;
    }

    public MECJsonRequest getNewRequestWithRedirectedUrl() {

        MECJsonRequest requestWithNewUrl = null;
        try {
            requestWithNewUrl = new MECJsonRequest(mRequest.getMethod(), getLocation(), mRequest.getParams(),
                    null, mRequest.getErrorListener());
        } catch (AuthFailureError authFailureError) {
            logError(authFailureError);
        }
        return requestWithNewUrl;
    }

    private void logError(AuthFailureError authFailureError) {
        final String message = authFailureError.getMessage();
        if(message!=null) {
            MECLog.e(MECLog.LOG, authFailureError.getMessage());
        }else {
            MECLog.e(MECLog.LOG, "auth failure while creating new request for Url redirection");
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
