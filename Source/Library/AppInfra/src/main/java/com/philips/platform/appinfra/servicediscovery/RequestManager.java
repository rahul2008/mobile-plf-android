package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by 310243577 on 1/5/2017.
 */

public class RequestManager {

    private static final String TAG = "RequestManager";//this.class.getSimpleName();

    private Context mContext = null;
    private AppInfra mAppInfra;

    public RequestManager(Context context, AppInfra appInfra) {
        this.mContext = context;
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
            ServiceDiscovery.Error volleyError = null;
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
            } // TODO RayKlo
            result.setError(volleyError);
        }
        return result;
    }

    private ServiceDiscovery parseResponse(JSONObject response) {
        ServiceDiscovery result = new ServiceDiscovery();
        try {
            result.setSuccess(response.optBoolean("success"));
            if (!result.isSuccess()) {
                ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
                result.setError(err);
            } else { // no sense in further processing if server reports error
                result = new Gson().fromJson(response.toString(), ServiceDiscovery.class);
            }
        } catch (Exception e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
            result.setSuccess(false);
            result.setError(err);
            e.printStackTrace();
        }
        return result;
    }
}


