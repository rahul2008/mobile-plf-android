package com.philips.platform.appinfra.servicediscovery;


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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.RequestFuture;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The RequestManager class for service discovery.
 */
public class RequestManager {

	//    RequestQueue mRequestQueue;
	private static final String TAG = "RequestManager";//this.class.getSimpleName();
	private final AppInfra mAppInfra;
	private static final String SERVICE_DISCOVERY_CACHE_FILE = "SDCacheFile";
	private final Context mContext;
	private SharedPreferences mSharedPreference;
	private SharedPreferences.Editor mPrefEditor;

	public RequestManager(Context context, AppInfra appInfra) {
		mContext = context;
		mAppInfra = appInfra;
		VolleyLog.DEBUG = false;
	}

	public ServiceDiscovery execute(final String url, ServiceDiscoveryManager.AISDURLType urlType) {
		final RequestFuture<JSONObject> future = RequestFuture.newFuture();
		final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future, null, null, null);
		request.setShouldCache(true);
		mAppInfra.getRestClient().getRequestQueue().add(request);

		final ServiceDiscovery result = new ServiceDiscovery(mAppInfra);
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
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SERVICE_DISCOVERY,"ServiceDiscovery error");
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "TimeoutORNoConnection");
			} else if (error instanceof NoConnectionError) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,  AppInfraLogEventID.AI_SERVICE_DISCOVERY,"ServiceDiscovery error");
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "NoConnectionError");
			} else if (error instanceof AuthFailureError) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SERVICE_DISCOVERY,"ServiceDiscovery error");
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "AuthFailureError");
			} else if (error instanceof ServerError) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SERVICE_DISCOVERY,"ServiceDiscovery error"+
						error.toString());
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
			} else if (error instanceof NetworkError) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SERVICE_DISCOVERY,"ServiceDiscovery error"+
						error.toString());
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "NetworkError");
			} else if (error instanceof ParseError) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "ServiceDiscovery error");
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
			} else {
				volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, "error while execute");
			}
			result.setError(volleyError);
		}
		return result;
	}

	private ServiceDiscovery parseResponse(JSONObject response) {
		final ServiceDiscovery result = new ServiceDiscovery(mAppInfra);
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
		mSharedPreference = getServiceDiscoverySharedPreferences();
		mPrefEditor = mSharedPreference.edit();
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
		mSharedPreference = getServiceDiscoverySharedPreferences();
		if (mSharedPreference != null) {
			if(getPropositionEnabled(mAppInfra))
			{
				final String platformCache = mSharedPreference.getString("SDPLATFORM", null);
				try {
					if (platformCache != null) {

						final JSONObject platformObject = new JSONObject(platformCache);
						final ServiceDiscovery platformService = parseResponse(platformObject);
						cachedResponse = new AISDResponse(mAppInfra);
						cachedResponse.setPlatformURLs(platformService);
						return cachedResponse;
					}
				} catch (Exception exception) {
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
							AppInfraLogEventID.AI_SERVICE_DISCOVERY, "while getting cached data"+exception.getMessage());
				}
			}
			else {
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
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
							AppInfraLogEventID.AI_SERVICE_DISCOVERY, "while getting cached data"+exception.getMessage());
				}
			}
		}
		return cachedResponse;
	}

	String getUrlProposition() {
		mSharedPreference = getServiceDiscoverySharedPreferences();
		if (mSharedPreference != null) {
			return mSharedPreference.getString("SDPROPOSITIONURL", null);
		}
		return null;
	}

	String getUrlPlatform() {
		mSharedPreference = getServiceDiscoverySharedPreferences();
		if (mSharedPreference != null) {
			return mSharedPreference.getString("SDPLATFORMURL", null);
		}
		return null;
	}

	boolean isServiceDiscoveryDataExpired() {
		mSharedPreference = getServiceDiscoverySharedPreferences();
		if (mSharedPreference != null) {
			final long refreshTimeExpiry = mSharedPreference.getLong("SDrefreshTime", 0);
			final Date currentDate = new Date();
			long currentDateLong = currentDate.getTime();
			return currentDateLong >= refreshTimeExpiry;
		}
		return false;
	}


	void clearCacheServiceDiscovery() {
		mSharedPreference = getServiceDiscoverySharedPreferences();
		mPrefEditor = mSharedPreference.edit();
		mPrefEditor.clear();
		mPrefEditor.commit();
	}

	private SharedPreferences getServiceDiscoverySharedPreferences() {
		return mContext.getSharedPreferences(SERVICE_DISCOVERY_CACHE_FILE, Context.MODE_PRIVATE);
	}

	boolean getPropositionEnabled(AppInfra appInfra) {
		final AppConfigurationInterface.AppConfigurationError appConfigurationError = new AppConfigurationInterface
				.AppConfigurationError();

		try {
			final Object propositionEnabledObject =  appInfra.getConfigInterface()
					.getPropertyForKey("servicediscovery.propositionEnabled", "appinfra",
          							appConfigurationError);
           if(propositionEnabledObject!=null) {
			   if(propositionEnabledObject instanceof Boolean) {
				   final Boolean propositionEnabled = (Boolean) propositionEnabledObject;
				   if (appConfigurationError.getErrorCode() == AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.NoError && !propositionEnabled) {
					   return true;
				   }
			   }else {
				   mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "servicediscovery.propositionEnabled instance should be boolean value true or false");
			   }
		   }
		} catch (IllegalArgumentException illegalArgumentException) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SERVICE_DISCOVERY, "IllegalArgumentException while getPropositionEnabled");
		}
		return false;
	}

}
