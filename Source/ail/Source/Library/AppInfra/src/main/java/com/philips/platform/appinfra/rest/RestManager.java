/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.io.File;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * The Rest webservice request class .
 */
public class RestManager implements RestInterface {

    private static final long serialVersionUID = -5276610949381468217L;
    private transient RequestQueue mRequestQueue;
    private AppInfra mAppInfra;

    public RestManager(AppInfra appInfra) {
        mAppInfra = appInfra;
        VolleyLog.DEBUG = false;
    }

    @Override
    public RequestQueue getRequestQueue() {
        Integer cacheSizeinKB = null;

        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone ,passes one in.
            // Instantiate the cache
            ;
            final AppConfigurationInterface mAppConfigurationInterface = mAppInfra.getConfigInterface();
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            if (mAppInfra.getConfigInterface() != null) {
                try {
                    cacheSizeinKB = (Integer) mAppConfigurationInterface.getPropertyForKey("restclient.cacheSizeInKB", "appinfra", configError);
                } catch (IllegalArgumentException i) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_REST,"CONFIG ERROR while getRequestQueue");
                }
            }
            if (cacheSizeinKB == null) {
                cacheSizeinKB = 1024; // default fall back
            }
            final Cache cache = new DiskBasedCache(getCacheDir(), cacheSizeinKB, mAppInfra); //

            // Set up the network to use HttpURLConnection as the HTTP client.
            final Network network = getNetwork();
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            // mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    public NetworkTypes  getNetworkReachabilityStatus() {
        NetworkTypes networkStatus=NetworkTypes.NO_NETWORK;
        final NetworkInfo connectionInfo = getNetworkInfo();
        if (null != connectionInfo && connectionInfo.isConnected()) {
            if (connectionInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkStatus = NetworkTypes.WIFI;
            } else if (connectionInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                networkStatus = NetworkTypes.MOBILE_DATA;
            }
        }
        return networkStatus;
    }

    @Override
    public boolean isInternetReachable() {
        final NetworkInfo networkInfo = getNetworkInfo();
        return (null != networkInfo && networkInfo.isConnected());
    }

    private NetworkInfo getNetworkInfo() {
        //Check for mobile data or Wifi network Info
        final ConnectivityManager connMgr = (ConnectivityManager) mAppInfra.getAppInfraContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo();
    }

    private Network getNetwork() {
        HttpStack stack = null;
        try {
            stack = new HurlStack(new ServiceIDResolver(), new TLSSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_REST," ERROR while getting network");
        }
        return new BasicNetwork(stack);
    }

    public static HashMap<String, String> setTokenProvider(TokenProviderInterface provider) {
        final HashMap<String, String> header = new HashMap<String, String>();
        final TokenProviderInterface.Token token = provider.getToken();
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

    private class ServiceIDResolver implements HurlStack.UrlRewriter {

        @Override
        public String rewriteUrl(final String originalUrl) {
            if (!ServiceIDUrlFormatting.isServiceIDUrl(originalUrl))
                return originalUrl;

            // final Lock lock = new ReentrantLock();
            //final Condition waitResult = lock.newCondition();
            final StringBuilder resultURL = new StringBuilder();
            //lock.lock();

            final String sid = ServiceIDUrlFormatting.getServiceID(originalUrl);
            try {
                if (ServiceIDUrlFormatting.getPreference(originalUrl) == ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE) {
                    mAppInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference(sid, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                        @Override
                        public void onSuccess(URL url) {
                            resultURL.append(url);
                        }

                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_REST, "REST"+error.toString());
                        }
                    });
                } else {
                    mAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(sid, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                        @Override
                        public void onSuccess(URL url) {
                            resultURL.append(url);
                        }

                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_REST, "REST"+error.toString());
                        }
                    });
                }
                //  waitResult.await();
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_REST, "REST ERROR");
            } finally {
                //waitResult.signalAll();
                //lock.unlock();
                if (resultURL.length() > 0)
                    resultURL.append(ServiceIDUrlFormatting.getUrlExtension(originalUrl));
            }
            if (resultURL.length() == 0)
                return null;
            if(resultURL.toString().contains("v1/")) {
                return  resultURL.toString().replace("v1/","v1");
            }
            return resultURL.toString();
        }

    }
}
