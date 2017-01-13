package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310243577 on 1/10/2017.
 */

import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//import android.os.LocaleList;

public class RequestManager {

    //    RequestQueue mRequestQueue;
    private static final String TAG = "RequestManager";//this.class.getSimpleName();
    private AppInfra mAppInfra;

    public RequestManager(AppInfra appInfra) {
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
            return parseResponse(response);
        } catch (InterruptedException | TimeoutException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Timed out or interrupted");
            result.setError(err);
            result.setSuccess(false);
        } catch (ExecutionException e) {
            Throwable error = e.getCause();
            ServiceDiscovery.Error volleyError;
            if (error instanceof TimeoutError) {
                Log.i("TimeoutORNoConnection", "" + "TimeoutORNoConnection");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "TimeoutORNoConnection");
            } else if (error instanceof NoConnectionError) {
                Log.i("NoConnectionError", "" + "NoConnectionError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "NoConnectionError");
            } else if (error instanceof AuthFailureError) {
                Log.i("AuthFailureError", "" + "AuthFailureError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "AuthFailureError");
            } else if (error instanceof ServerError) {
                Log.i("ServerError", "" + "ServerError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
            } else if (error instanceof NetworkError) {
                Log.i("NetworkError", "" + "NetworkError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "NetworkError");
            } else if (error instanceof ParseError) {
                Log.i("ParseError", "" + "ParseError");
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
            result.parseResponse(mAppInfra, response);
        }

        return result;
    }


    public String getLocaleList() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().toString();
        } else {
            return mAppInfra.getInternationalization().getUILocaleString();
        }
    }
}
