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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.AIImageRequest;
import com.philips.platform.appinfra.rest.request.AIJsonObjectRequest;
import com.philips.platform.appinfra.rest.request.AIStringRequest;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RestManager implements RestInterface {
    private RequestQueue mRequestQueue;
    private AppInfra mAppInfra;

    public RestManager(AppInfra appInfra) {
        mAppInfra = appInfra;
    }


    @Override
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone ,passes one in.
            // Instantiate the cache
            AppConfigurationInterface mAppConfigurationInterface;
            mAppConfigurationInterface = mAppInfra.getConfigInterface();
            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            Integer cacheSizeinKB = null;
            try {
                cacheSizeinKB = (Integer) mAppConfigurationInterface.getPropertyForKey("restclient.cacheSizeInKB", "appinfra", configError);
            } catch (IllegalArgumentException i) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "CONFIG ERROR", i.toString());
            }
            if (cacheSizeinKB == null) {
                cacheSizeinKB = 1024; // default fall back
            }
            Cache cache = new DiskBasedCache(getCacheDir(), cacheSizeinKB, mAppInfra); //

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = getNetwork();
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            // mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private Network getNetwork() {
        return new BasicNetwork(new HurlStack(new ServiceIDResolver()));
    }

    @Override
    public HashMap<String, String> setTokenProvider(TokenProviderInterface provider) {
        HashMap<String, String> header = new HashMap<String, String>();
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



    private File getCacheDir() {
        return mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private boolean isValidURL(String url) {
        boolean isValidurl = false;
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (null != url && url.matches(regex)) {
            isValidurl = true;
        }
        return isValidurl;
    }

    protected class ServiceIDResolver implements HurlStack.UrlRewriter {

        public String rewriteUrl(String originalUrl) {
            if (!ServiceIDUrlFormatting.isServiceIDUrl(originalUrl))
                return originalUrl;

            final Lock lock = new ReentrantLock();
            final Condition waitResult = lock.newCondition();
            final StringBuilder resultURL = new StringBuilder();

            String sid = ServiceIDUrlFormatting.getServiceID(originalUrl);
            try {
                if (ServiceIDUrlFormatting.getPreference(originalUrl) == ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE) {
                    mAppInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference(sid, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                        @Override
                        public void onSuccess(URL url) {
                            resultURL.append(url);
                        }

                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR , "REST" , error.toString());
                        }
                    });
                }
                else {
                    mAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(sid, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                        @Override
                        public void onSuccess(URL url) {
                            resultURL.append(url);
                        }

                        @Override
                        public void onError(ERRORVALUES error, String message) {
                        }
                    });
                }
                waitResult.await();
            }
            catch (InterruptedException e) {

            }
            finally {
                if (resultURL.length()==0)
                    return null;
                resultURL.append(ServiceIDUrlFormatting.getUrlExtension(originalUrl));
                return resultURL.toString();
            }
        }

    }


}
