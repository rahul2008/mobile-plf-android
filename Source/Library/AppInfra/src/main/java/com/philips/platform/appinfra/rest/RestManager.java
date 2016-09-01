package com.philips.platform.appinfra.rest;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.philips.platform.appinfra.AppInfra;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 310238655 on 8/26/2016.
 */

public class RestManager implements RestInterface{
    private RequestQueue mRequestQueue ;
    private static Context mCtx;
    AppInfra mAppInfra;

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
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
