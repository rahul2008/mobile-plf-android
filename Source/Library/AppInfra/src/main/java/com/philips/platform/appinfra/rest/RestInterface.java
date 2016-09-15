package com.philips.platform.appinfra.rest;

import com.android.volley.RequestQueue;

import java.util.HashMap;


/**
 * Created by 310238655 on 8/24/2016.
 */
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
