/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;
import com.philips.platform.appinfra.rest.request.ImageRequest;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.rest.request.StringRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class RestManager implements RestInterface{
    private RequestQueue mRequestQueue ;
    public static AppInfra mAppInfra;
    private AppConfigurationInterface mAppConfigurationInterface;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetServiceUrlListener mOnGetServiceUrlListener = null;
    public final static String LANGUAGE="language";
    public final static String COUNTRY="country";
    enum RequestData {StringRequest,JsonObjectRequest,ImageRequest};

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

    @Override
    public void stringRequestWithServiceID(final int requestType, String serviceID, String serviceDiscoveryPreference, final String pathComponent, final ServiceIDCallback listener, final Map<String, String> headers,final  Map<String, String> params)  throws HttpForbiddenException {
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        if(serviceDiscoveryPreference.equalsIgnoreCase(LANGUAGE)) {
            mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url)  {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        stringRequestWithURL(requestType,urlString,listener,headers,params);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString());
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }else if (serviceDiscoveryPreference.equalsIgnoreCase(COUNTRY)) {
            mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        stringRequestWithURL(requestType,urlString,listener,headers,params);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString());
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }

    }

    @Override
    public void jsonObjectRequestWithServiceID(final int requestType, String serviceID, String serviceDiscoveryPreference, final String pathComponent, final ServiceIDCallback listener,final Map<String, String> headers,final Map<String, String> params) throws HttpForbiddenException {
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        if(serviceDiscoveryPreference.equalsIgnoreCase(LANGUAGE)) {
            mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url)  {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        jsonObjectRequestWithURL(requestType,urlString,listener,headers,params);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString() );
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }else if (serviceDiscoveryPreference.equalsIgnoreCase(COUNTRY)) {
            mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        jsonObjectRequestWithURL(requestType,urlString,listener,headers,params);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString() );
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }
    }

    @Override
    public void imageRequestWithServiceID( String serviceID, String serviceDiscoveryPreference,final String pathComponent,final  ServiceIDCallback listener,final Map<String, String> headers,final  ImageView.ScaleType scaleType, final Bitmap.Config decodeConfig,final int maxWidth, final int maxHeight) throws HttpForbiddenException {
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        if(serviceDiscoveryPreference.equalsIgnoreCase(LANGUAGE)) {
            mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url)  {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        imageRequestWithURL(urlString,listener,headers,scaleType,decodeConfig,maxWidth,maxHeight);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString() );
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }else if (serviceDiscoveryPreference.equalsIgnoreCase(COUNTRY)) {
            mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(serviceID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                @Override
                public void onSuccess(URL url) {
                    String urlString= url.toString();
                    if(null!=pathComponent){
                        urlString.concat(pathComponent);
                    }
                    try {
                        imageRequestWithURL(urlString,listener,headers,scaleType,decodeConfig,maxWidth,maxHeight);
                    } catch (HttpForbiddenException e) {
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",e.toString());
                        listener.onErrorResponse(e.toString() );
                    }
                }
                @Override
                public void onError(ERRORVALUES error, String message) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SD ERROR",error.toString()+" "+message);
                    listener.onErrorResponse(error.toString() +" "+message);
                }
            });
        }
    }

    private void stringRequestWithURL(final int requestType, String urlString, final ServiceIDCallback listener, final Map<String, String> headers,final  Map<String, String> params) throws HttpForbiddenException {
        StringRequest request=null;
        try {
               // request= new StringRequest(requestType, urlString, new Response.Listener<String>() {
                request= new StringRequest(requestType, "https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(100).limit.(1).json", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response);
                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"VOLLEY ERROR",error.toString());
                            listener.onErrorResponse(error.toString());
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramList = new HashMap<String, String>();
                    if (null != paramList && paramList.size() > 0){
                        for (String key : params.keySet()) {
                            paramList.put(key, params.get(key));
                        }
                     }
                    return paramList;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headerList = new HashMap<String, String>();
                    if (null != headers && headers.size() > 0) {
                        for (String key : headers.keySet()) {
                            headerList.put(key, headers.get(key));
                        }
                    }
                    return headerList;
                }
            };                             ;
        } catch (HttpForbiddenException e) {
            e.printStackTrace();
        }
        getRequestQueue().add(request);
    }

    private void  jsonObjectRequestWithURL(int requestType,String urlString,final ServiceIDCallback listener,final Map<String, String>headers, final Map<String, String>params) throws HttpForbiddenException{
        JSONObject jsonObjectParams =null;
        if (null != params && params.size() > 0){
            jsonObjectParams= new JSONObject(params);
        }
        urlString="https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(100).limit.(1).json";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestType, urlString, jsonObjectParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error.toString());
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerList = new HashMap<String, String>();
                if (null != headers && headers.size() > 0) {
                    for (String key : headers.keySet()) {
                        headerList.put(key, headers.get(key));
                    }
                }
                return headerList;
            }
        }
                ;
        getRequestQueue().add(jsObjRequest);

    }

    private void imageRequestWithURL(String urlString, final ServiceIDCallback listener, Map<String, String> headers, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, int maxWidth, int maxHeight) throws HttpForbiddenException{
        urlString = "http://i.imgur.com/7spzG.png";
        ImageRequest imageRequest = new ImageRequest(urlString,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        listener.onSuccess(bitmap);
                        //mImageView.setImageBitmap(bitmap);
                    }
                }, maxWidth, maxHeight,scaleType, decodeConfig,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error.toString());
                        //mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });
        getRequestQueue().add(imageRequest);

    }
    private File getCacheDir(){
        return  mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
