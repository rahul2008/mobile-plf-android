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
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class RequestManager {

	//    RequestQueue mRequestQueue;
	private final AppInfra mAppInfra;
	private static final String SERVICE_DISCOVERY_CACHE_FILE = "SDCacheFile";
	private final Context mContext;
	private SharedPreferences mSharedPreference;
	private SharedPreferences.Editor mPrefEditor;

	public RequestManager(Context context, AppInfra appInfra) {
		mContext = context;
		mAppInfra = appInfra;
		mSharedPreference = getServiceDiscoverySharedPreferences();
		mPrefEditor = mSharedPreference.edit();
	}

	public ServiceDiscovery execute(final String url, ServiceDiscoveryManager.AISDURLType urlType) {
		final RequestFuture<JSONObject> future = RequestFuture.newFuture();
		final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future, null, null, null);
		request.setShouldCache(true);
		mAppInfra.getRestClient().getRequestQueue().add(request);

		final ServiceDiscovery result = new ServiceDiscovery();
		try {
			final JSONObject response = future.get(10, TimeUnit.SECONDS);
			cacheServiceDiscovery(response, url, urlType);
			return parseResponse(response);
		} catch (InterruptedException | TimeoutException e) {
			final ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface
					.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Timed out or interrupted");
			result.setError(err);
			result.setSuccess(false);
		} catch (ExecutionException e) {
			final Throwable error = e.getCause();
			ServiceDiscovery.Error volleyError=null;
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
		final ServiceDiscovery result = new ServiceDiscovery();
		result.setSuccess(response.optBoolean("success"));
		if (!result.isSuccess()) {
			final ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
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

	private void cacheServiceDiscovery(JSONObject serviceDiscovery, String url, ServiceDiscoveryManager.AISDURLType urlType) {
		final Date currentDate = new Date();
		final long refreshTimeExpiry = currentDate.getTime() + 24 * 3600 * 1000;  // current time + 24 hour
		switch (urlType) {
			case AISDURLTypeProposition:
				mPrefEditor.putString("SDPROPOSITION", serviceDiscovery.toString());
				mPrefEditor.putString("SDPROPOSITIONURL", url);
				break;
			case AISDURLTypePlatform:
				mPrefEditor.putString("SDPLATFORM", serviceDiscovery.toString());
				mPrefEditor.putString("SDPLATFORMURL", url);
				break;
		}
		mPrefEditor.putLong("SDrefreshTime", refreshTimeExpiry);
		mPrefEditor.commit();
	}


	protected AISDResponse getCachedData() {
		AISDResponse cachedResponse = null;
		if (mSharedPreference != null) {
			final String propositionCache = mSharedPreference.getString("SDPROPOSITION", null);
			final String platformCache = mSharedPreference.getString("SDPLATFORM", null);
			try {
				if (propositionCache != null && platformCache != null) {
					final JSONObject propositionObject = new JSONObject(propositionCache);
					final ServiceDiscovery propostionService = parseResponse(propositionObject);

					final JSONObject platformObject = new JSONObject(platformCache);
					final ServiceDiscovery platformService = parseResponse(platformObject);
					cachedResponse = new AISDResponse(mAppInfra);
					cachedResponse.setPropositionURLs(propostionService);
					cachedResponse.setPlatformURLs(platformService);
					return cachedResponse;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return cachedResponse;
	}

	String getUrlProposition() {
		if (mSharedPreference != null) {
			return mSharedPreference.getString("SDPROPOSITIONURL", null);
		}
		return null;
	}

	String getUrlPlatform() {
		if (mSharedPreference != null) {
			return mSharedPreference.getString("SDPLATFORMURL", null);
		}
		return null;
	}

	boolean isServiceDiscoveryDataExpired() {
		if (mSharedPreference != null) {
			final long refreshTimeExpiry = mSharedPreference.getLong("SDrefreshTime", 0);
			final Date currentDate = new Date();
			long currentDateLong = currentDate.getTime();
			return currentDateLong >= refreshTimeExpiry;
		}
		return false;
	}


	void clearCacheServiceDiscovery() {
		final SharedPreferences.Editor prefEditor = mSharedPreference.edit();
		prefEditor.clear();
		prefEditor.commit();
	}

	private SharedPreferences getServiceDiscoverySharedPreferences() {
		return mContext.getSharedPreferences(SERVICE_DISCOVERY_CACHE_FILE, Context.MODE_PRIVATE);
	}

}
