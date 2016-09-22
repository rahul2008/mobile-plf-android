/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import com.android.volley.RequestQueue;

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

}
