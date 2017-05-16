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
	private String appUpdateUrl = "https://hashim-rest.herokuapp.com/sd/dev/appupdate/appinfra/version.json";
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
						mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI AppUpate", response.toString());
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
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI AppUpdate_URL", errMsg);
				refreshListener.onError(OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_FAILED, errMsg);

			}
		}, null, null, null);
		mAppInfra.getRestClient().getRequestQueue().add(jsonRequest);
	}

	private void processResponse(JSONObject response, OnRefreshListener refreshListener) {
		mAppUpdateModel = mGson.fromJson(response.toString(), AppupdateModel.class);
		if(mAppUpdateModel != null) {
			mHandler.post(postRefreshSuccess(refreshListener, OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_SUCCESS));
		} else {
			mHandler.post(postRefreshError(refreshListener, OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_FAILED , "Parsing Issue"));
		}
	}

	private Runnable postRefreshSuccess(final OnRefreshListener aIRefreshResult,
	                            final OnRefreshListener.AIAppUpdateRefreshResult ailpRefreshResult) {
		return new Runnable() {
			@Override
			public void run() {
				if (aIRefreshResult != null)
					aIRefreshResult.onSuccess(ailpRefreshResult);
			}
		};
	}

	private Runnable postRefreshError(final OnRefreshListener aIRefreshResult,
	                          final OnRefreshListener.AIAppUpdateRefreshResult ailpRefreshResult, final String errorDescription) {
		return new Runnable() {
			@Override
			public void run() {
				if (aIRefreshResult != null)
					aIRefreshResult.onError(ailpRefreshResult, errorDescription);
			}
		};
	}


	private Handler getHandler(Context context) {
		return new Handler(context.getMainLooper());
	}

	@Override
	public void refresh(OnRefreshListener refreshListener) {
			downloadAppUpdate(refreshListener);
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
