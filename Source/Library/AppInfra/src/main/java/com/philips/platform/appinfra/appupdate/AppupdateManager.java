package com.philips.platform.appinfra.appupdate;


import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appupdate.model.AppupdateModel;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppupdateManager implements AppupdateInterface {

	private Context mContext;
	private AppInfra mAppInfra;
	private String appUpdateUrl = "https://hashim-rest.herokuapp.com/sd/dev/appupdate/appinfra/version.json";
	private Handler mHandler;
	private AppupdateModel mAppUpdateModel;
	private Gson mGson;
	private FileUtils mFileUtils;

	public AppupdateManager(AppInfra appInfra) {
		this.mAppInfra = appInfra;
		this.mContext = appInfra.getAppInfraContext();
		mGson = new Gson();
		mFileUtils = new FileUtils(mContext);
		mAppUpdateModel = getAppUpdateModel();
	}

	private void downloadAppUpdate(final OnRefreshListener refreshListener) {
		JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, appUpdateUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI AppUpate", response.toString());
						try {
							final JSONObject resp = response.getJSONObject("android");
							mFileUtils.saveFile(resp.toString(), AppupdateConstants.LOCALE_FILE_DOWNLOADED, AppupdateConstants.APPUPDATE_PATH);
							mHandler = getHandler(mContext);
							new Thread(new Runnable() {
								@Override
								public void run() {
									processResponse(resp.toString(), refreshListener);
								}
							}).start();
						} catch (JSONException e) {
							mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI AppUpdate_URL", "JSON EXCEPTION");
						}
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

	private void processResponse(String response, OnRefreshListener refreshListener) {
		mAppUpdateModel = mGson.fromJson(response, AppupdateModel.class);
		if (mAppUpdateModel != null) {
			mHandler.post(postRefreshSuccess(refreshListener, OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_SUCCESS));
		} else {
			mHandler.post(postRefreshError(refreshListener, OnRefreshListener.AIAppUpdateRefreshResult.AIAppUpdate_REFRESH_FAILED, "Parsing Issue"));
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


	private AppupdateModel getAppUpdateModel() {
		if (mAppUpdateModel == null) {
			mAppUpdateModel = mGson.fromJson(mFileUtils.readFile(getAppUpdatefromCache()), AppupdateModel.class);
		}
		return mAppUpdateModel;
	}

	private File getAppUpdatefromCache() {
		final File file = mFileUtils.getLanguagePackFilePath(AppupdateConstants.LOCALE_FILE_DOWNLOADED
				, AppupdateConstants.APPUPDATE_PATH);
		return file;
	}

	private Handler getHandler(Context context) {
		return new Handler(context.getMainLooper());
	}

	@Override
	public void refresh(OnRefreshListener refreshListener) {
		downloadAppUpdate(refreshListener);
	}

	private String getAppVersion() {
		return mAppInfra.getAppIdentity().getAppVersion();
	}

	@Override
	public boolean isDeprecated() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getVersion() != null) {
			String minVer = getAppUpdateModel().getVersion().getMinimumVersion();
			String deprecatedVersion = getAppUpdateModel().getVersion().getDeprecatedVersion();
			String deprecationDate = getAppUpdateModel().getVersion().getDeprecationDate();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
			try {
				Date deprecationdate = formatter.parse(deprecationDate);
				Date currentDate = new Date();
				return AIVersion.isverionlessthancloud(getAppVersion(), minVer) ||
						AIVersion.isbothversionsame(getAppVersion(), deprecatedVersion) && currentDate.after(deprecationdate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public boolean isToBeDeprecated() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getVersion() != null) {
			String minVer = getAppUpdateModel().getVersion().getMinimumVersion();
			String deprecatedVer = getAppUpdateModel().getVersion().getDeprecatedVersion();
			return AIVersion.isverionlessthancloud(minVer, getAppVersion()) &&
					AIVersion.isversionlessthanequalsto(getAppVersion(), deprecatedVer);
		}
		return false;
	}

	@Override
	public boolean isUpdateAvailable() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getVersion() != null) {
			String currentVer = getAppUpdateModel().getVersion().getCurrentVersion();
			return AIVersion.isverionlessthancloud(getAppVersion(), currentVer);
		}
		return false;
	}

	@Override
	public String getDeprecateMessage() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getMessages() != null) {
			return getAppUpdateModel().getMessages().getMinimumVersionMessage();
		}
		return null;
	}

	@Override
	public String getToBeDeprecatedMessage() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getMessages() != null) {
			return getAppUpdateModel().getMessages().getDeprecatedVersionMessage();
		}
		return null;
	}

	@Override
	public String getToBeDeprecatedDate() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getVersion() != null) {
			return getAppUpdateModel().getVersion().getDeprecationDate();
		}
		return null;
	}

	@Override
	public String getUpdateMessage() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getMessages() != null) {
			return getAppUpdateModel().getMessages().getCurrentVersionMessage();
		}
		return null;
	}

	@Override
	public String getMinimumVersion() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getVersion() != null) {
			return getAppUpdateModel().getVersion().getMinimumVersion();
		}
		return null;
	}

	@Override
	public String getMinimumOSverion() {
		if (getAppUpdateModel() != null && getAppUpdateModel().getRequirements() != null) {
			return getAppUpdateModel().getRequirements().getMinimumOSVersion();
		}
		return null;
	}
}
