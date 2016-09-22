/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.io.File;
import java.util.HashMap;



public class RestManager implements RestInterface{
    private RequestQueue mRequestQueue ;
    AppInfra mAppInfra;
    private AppConfigurationInterface mAppConfigurationInterface;


    public RestManager( AppInfra appInfra) {
        mAppInfra = appInfra;
    }

//    public static synchronized RestManager getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new RestManager(context);
//        }
//        return mInstance;
//    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone ,passes one in.
            // Instantiate the cache
            mAppConfigurationInterface = mAppInfra.getConfigInterface();
            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            Integer cacheSizeinKB = (Integer)mAppConfigurationInterface.getPropertyForKey("restclient.cacheSizeInKB","appinfra",configError);
            if(cacheSizeinKB==null  ) {
                cacheSizeinKB = 1024; // default fall back
            }
            Cache cache = new DiskBasedCache(getCacheDir(), cacheSizeinKB, mAppInfra); //

// Set up the network to use HttpURLConnection as the HTTP client.
            Network network = getNetwork();
            mRequestQueue = new RequestQueue(cache,network);
            mRequestQueue.start();
           // mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    protected Network getNetwork(){
        return new BasicNetwork(new HurlStack());
    }

    @Override
    public HashMap<String, String> setTokenProvider(TokenProviderInterface provider) {
        HashMap<String, String> header = new  HashMap<String, String>();
        TokenProviderInterface.Token token = provider.getToken();
        String scheme = "";
        if (token.getTokenType() == TokenProviderInterface.TokenType.OAUTH2)
            scheme = "Bearer";
        else
            throw new IllegalArgumentException("unsupported token type");
       // String header = "Authorization: " + scheme + " " + token.getTokenValue();
        header.put("Authorization", scheme + " " + token.getTokenValue());

        return header;
    }

    private File getCacheDir(){
        return  mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }



}
