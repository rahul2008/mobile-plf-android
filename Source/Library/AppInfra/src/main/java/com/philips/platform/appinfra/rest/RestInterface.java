/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import com.philips.platform.appinfra.rest.request.RequestQueue;

import java.util.HashMap;


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

}
