package com.philips.platform.appinfra.rest;

import com.android.volley.RequestQueue;

import java.util.HashMap;


/**
 * Created by 310238655 on 8/24/2016.
 */
public interface RestInterface {


    public RequestQueue getRequestQueue();
    public HashMap<String, String> setTokenProvider(TokenProviderInterface provider);

}
