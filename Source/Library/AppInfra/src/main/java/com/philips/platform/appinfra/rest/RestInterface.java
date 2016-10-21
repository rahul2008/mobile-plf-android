/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;

import java.util.HashMap;
import java.util.Map;


public interface RestInterface {


    /**
     * Gets request queue.
     *
     * @return the REST request queue
     */
    public RequestQueue getRequestQueue();

    /**
     * Sets token provider.
     *
     * @param provider the token provider which needs to be implemented by verticals
     * @return the token provider
     */
    public HashMap<String, String> setTokenProvider(TokenProviderInterface provider);


    /**
     * This is the callback method Error cases.
     * the call back will have Error method for actions completed with Errors.
     * onSuccess returns the Error response type
     */
    interface OnErrorListener {
        enum ERRORVALUES {
            NO_NETWORK, CONNECTION_TIMEOUT, SERVER_ERROR, SECURITY_ERROR,
            INVALID_RESPONSE
        }
        void onError(ERRORVALUES error, String message);
    }

    /**
     * This is the callback method from getServiceUrlWithLanguagePreference() API.
     * the call back will have success method for actions completed successfully.
     * onSuccess returns the successful response
     */

    interface OnGetServiceUrlListener extends OnErrorListener {
        void onSuccess(String url);
    }

    public  void stringRequestWithServiceID(final int requestType, String serviceID, String serviceDiscoveryPreference, final String pathComponent, final ServiceIDCallback listener, Map<String, String> headers, Map<String, String> params ) throws HttpForbiddenException;
    public  void jsonObjectRequestWithServiceID(final int requestType, String serviceID, String serviceDiscoveryPreference, final String pathComponent, final ServiceIDCallback listener, Map<String, String> headers, Map<String, String> params ) throws HttpForbiddenException;
    public  void imageRequestWithServiceID( String serviceID, String serviceDiscoveryPreference, final String pathComponent, final ServiceIDCallback listener, Map<String, String> headers, final ImageView.ScaleType scaleType, final Bitmap.Config decodeConfig, int maxWidth, int maxHeight ) throws HttpForbiddenException;

    public interface ServiceIDCallback{
        void onSuccess(Object response);
        void onErrorResponse(String error);
    }

}
