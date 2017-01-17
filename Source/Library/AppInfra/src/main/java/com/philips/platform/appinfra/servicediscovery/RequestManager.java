package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310243577 on 1/10/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class RequestManager {

    //    RequestQueue mRequestQueue;
    private static final String TAG = "RequestManager";//this.class.getSimpleName();
    private AppInfra mAppInfra;
    private static final String ServiceDiscoveryCacheFile = "SDCacheFile";
    private Context mContext = null;

    public RequestManager(Context context, AppInfra appInfra) {
        mContext = context;
        mAppInfra = appInfra;
    }

    public ServiceDiscovery execute(final String url) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future, null, null, null);
        request.setShouldCache(true);
        mAppInfra.getRestClient().getRequestQueue().add(request);

        ServiceDiscovery result = new ServiceDiscovery();

        try {
            JSONObject response = future.get(10, TimeUnit.SECONDS); // Blocks for at most 10 seconds.
            cacheServiceDiscovery(response, url);
            return parseResponse(response);
        } catch (InterruptedException | TimeoutException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Timed out or interrupted");
            result.setError(err);
            result.setSuccess(false);
        } catch (ExecutionException e) {
            Throwable error = e.getCause();
            ServiceDiscovery.Error volleyError;
            if (error instanceof TimeoutError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "TimeoutORNoConnection");
            } else if (error instanceof NoConnectionError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "NoConnectionError");
            } else if (error instanceof AuthFailureError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "AuthFailureError");
            } else if (error instanceof ServerError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
            } else if (error instanceof NetworkError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "NetworkError");
            } else if (error instanceof ParseError) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ServiceDiscovery error",
                        error.toString());
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
            } else {
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, e.getMessage());
            }
            result.setError(volleyError);
        }
        return result;
    }

    private ServiceDiscovery parseResponse(JSONObject response) {
        ServiceDiscovery result = new ServiceDiscovery();
        result.setSuccess(response.optBoolean("success"));
        if (!result.isSuccess()) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
            result.setError(err);
        } else { // no sense in further processing if server reports error
            // START setting match by country
            result.parseResponse(mContext, mAppInfra, response);
        }

        return result;
    }


    public String getLocaleList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().toString();
        } else {
            return mAppInfra.getInternationalization().getUILocaleString();
        }
    }

    private void cacheServiceDiscovery(JSONObject serviceDiscovery, String url) {
        SharedPreferences sharedPreferences = getServiceDiscoverySharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString("SDcache", serviceDiscovery.toString());
        prefEditor.putString("SDurl", url);
        Date currentDate = new Date();
        long refreshTimeExpiry = currentDate.getTime() + 24 * 3600 * 1000;  // current time + 24 hour
        prefEditor.putLong("SDrefreshTime", refreshTimeExpiry);
        prefEditor.commit();
    }

    ServiceDiscovery getServiceDiscoveryFromCache(String url) {
        ServiceDiscovery serviceDiscovery = null;
        SharedPreferences prefs = getServiceDiscoverySharedPreferences();
        if (null != prefs && prefs.contains("SDurl")) {
            final String savedURL = prefs.getString("SDurl", null);
            final long refreshTimeExpiry = prefs.getLong("SDrefreshTime", 0);
            Date currentDate = new Date();
            long currentDateLong = currentDate.getTime();
            if (savedURL.equals(url) && currentDateLong < refreshTimeExpiry) { // cache is VALID  i.e. AppIdentity and locale has not changed
                try {
                    JSONObject SDjSONObject = new JSONObject(prefs.getString("SDcache", null));
                    serviceDiscovery = parseResponse(SDjSONObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {// cache is INVALID so clear SD Cache
                clearCacheServiceDiscovery();
            }
        }
        return serviceDiscovery;
    }

    void clearCacheServiceDiscovery() {
        SharedPreferences prefs = getServiceDiscoverySharedPreferences();
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.clear();
        prefEditor.commit();
    }

    private SharedPreferences getServiceDiscoverySharedPreferences() {
        return mContext.getSharedPreferences(ServiceDiscoveryCacheFile, Context.MODE_PRIVATE);
    }

}
