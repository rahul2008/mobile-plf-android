package com.philips.platform.appinfra.rest;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;

import java.util.List;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;


/**
 * Created by 310238655 on 8/24/2016.
 */
public interface RestInterface {


    public RequestQueue getRequestQueue();


}
