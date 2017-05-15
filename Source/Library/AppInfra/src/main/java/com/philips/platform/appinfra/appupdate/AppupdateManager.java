package com.philips.platform.appinfra.appupdate;


import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appupdate.model.AppupdateModel;
import com.philips.platform.appinfra.languagepack.LanguagePackUtil;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

import org.json.JSONObject;

public class AppupdateManager implements AppupdateInterface {

	private Context mContext;
	private AppInfra mAppInfra;
	private String appUpdateUrl = "http://hashim-rest.herokuapp.com/sd/dev/appupdate/appinfra/version.json";
	private Handler mHandler;
	private AppupdateModel mAppUpdateModel;
	private Gson mGson;

	public AppupdateManager(AppInfra appInfra) {
		this.mAppInfra = appInfra;
		this.mContext = appInfra.getAppInfraContext();
		mGson = new Gson();
	}

	private void downloadAppUpdate(final OnRefreshListener refreshListener) {
		JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, appUpdateUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AIAppUpate", response.toString());
						LanguagePackUtil util = new LanguagePackUtil(mContext);
						util.saveFile(response.toString(),AppupdateConstants.LOCALE_FILE_DOWNLOADED,AppupdateConstants.APPUPDATE_PATH);

						mHandler = getHandler(mContext);
						new Thread(new Runnable() {
							@Override
							public void run() {
								processResponse(response, refreshListener);
							}
						}).start();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				final String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
				final String errMsg = " Error Code:" + errorcode + " , Error Message:" + error.toString();
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AILP_URL", errMsg);
				refreshListener.onError(OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_FAILED, errMsg);

			}
		}, null, null, null);
		mAppInfra.getRestClient().getRequestQueue().add(jsonRequest);
	}

	private void processResponse(JSONObject response, OnRefreshListener refreshListener) {
		mAppUpdateModel = mGson.fromJson(response.toString(), AppupdateModel.class);
		if(mAppUpdateModel != null) {
			refreshListener.onError(OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_FAILED, "Parsing Issue");
		} else {
			refreshListener.onError(OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_SUCCESS, "AppUpdate Download Success");
		}
	}

	private boolean isappUpdateRequired() {
		try {
			AppConfigurationInterface.AppConfigurationError configurationError = new AppConfigurationInterface.AppConfigurationError();
			boolean isappUpdateRq = (boolean) mAppInfra.getConfigInterface().getPropertyForKey("appinfra.appupdate","appinfra",configurationError);
		    return isappUpdateRq;
		} catch (IllegalArgumentException exception) {

		}
		return false;
	}

	private Handler getHandler(Context context) {
		return new Handler(context.getMainLooper());
	}

	@Override
	public void refresh(OnRefreshListener refreshListener) {

	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isToBeDeprecated() {
		return false;
	}

	@Override
	public boolean isUpdateAvailable() {
		return false;
	}

	@Override
	public String getDeprecateMessage() {
		return null;
	}

	@Override
	public String getToBeDeprecatedMessage() {
		return null;
	}

	@Override
	public String getToBeDeprecatedDate() {
		return null;
	}

	@Override
	public String getUpdateMessage() {
		return null;
	}

	@Override
	public String getMinimumVersion() {
		return null;
	}

	@Override
	public String getMinimumOSverion() {
		return null;
	}
}
