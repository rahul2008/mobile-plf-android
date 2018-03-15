/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.net.HttpURLConnection;

import static com.philips.cdp.di.iap.utils.IAPConstant.HTTP_REDIRECT;

public class IAPUrlRedirectionHandler {

    IAPJsonRequest mRequest;
    VolleyError volleyError;

    public IAPUrlRedirectionHandler(IAPJsonRequest request, VolleyError error){
        mRequest = request;
        volleyError = error;
    }

    public IAPJsonRequest getNewRequestWithRedirectedUrl() {
        final String location = volleyError.networkResponse.headers.get("Location");
        IAPJsonRequest requestWithNewUrl = null;
        try {
            requestWithNewUrl = new IAPJsonRequest(mRequest.getMethod(), location, mRequest.getParams(),
                    null, mRequest.getErrorListener());
        } catch (AuthFailureError authFailureError) {
            final String message = authFailureError.getMessage();
            if(message!=null) {
                IAPLog.e(IAPLog.LOG, authFailureError.getMessage());
            }else {
                IAPLog.e(IAPLog.LOG, "auth failure while creating new request for Url redirection");
            }
        }
        return requestWithNewUrl;
    }

    public boolean isRedirectionRequired() {
        final int status = volleyError.networkResponse.statusCode;
        return status == HTTP_REDIRECT || HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER;
    }
}
