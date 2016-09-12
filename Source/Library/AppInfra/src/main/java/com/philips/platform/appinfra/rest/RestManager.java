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

/**
 * Created by 310238655 on 8/26/2016.
 */

public class RestManager implements RestInterface{
    private RequestQueue mRequestQueue ;
    private static Context mCtx;
    AppInfra mAppInfra;
    private AppConfigurationInterface mAppConfigurationInterface;


    public RestManager(Context context, AppInfra appInfra) {
        mCtx = context;
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
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache,network);
            mRequestQueue.start();
           // mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private File getCacheDir(){
        return  mCtx.getApplicationContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }



}
