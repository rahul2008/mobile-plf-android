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
							JSONObject resp = response.getJSONObject("android");
							mFileUtils.saveFile(resp.toString(), AppupdateConstants.LOCALE_FILE_DOWNLOADED, AppupdateConstants.APPUPDATE_PATH);
							mHandler = getHandler(mContext);
							new Thread(new Runnable() {
								@Override
								public void run() {
									processResponse(response, refreshListener);
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

	private void processResponse(JSONObject response, OnRefreshListener refreshListener) {
		mAppUpdateModel = mGson.fromJson(response.toString(), AppupdateModel.class);
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
		String minVer = getAppUpdateModel().getVersion().getMinimumVersion();
		String deprecatedVersion = getAppUpdateModel().getVersion().getDeprecatedVersion();
		String deprecationDate = getAppUpdateModel().getVersion().getDeprecationDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
		try {
			Date deprecationdate = formatter.parse(deprecationDate);
			Date currentDate = new Date();

			return compareVersion(getAppVersion(), minVer) || compareVersion(getAppVersion(), deprecatedVersion) && currentDate.after(deprecationdate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isToBeDeprecated() {
		String minVer = getAppUpdateModel().getVersion().getMinimumVersion();
		String deprecatedVer = getAppUpdateModel().getVersion().getDeprecatedVersion();
		return compareVersion(minVer, getAppVersion()) && compareVersion(getAppVersion(), deprecatedVer);
	}

	@Override
	public boolean isUpdateAvailable() {
		String currentVer = getAppUpdateModel().getVersion().getCurrentVersion();
		return compareVersion(getAppVersion(), currentVer);
	}

	@Override
	public String getDeprecateMessage() {
		return getAppUpdateModel().getMessages().getMinimumVersionMessage();
	}

	@Override
	public String getToBeDeprecatedMessage() {
		return getAppUpdateModel().getMessages().getDeprecatedVersionMessage();
	}

	@Override
	public String getToBeDeprecatedDate() {
		return getAppUpdateModel().getVersion().getDeprecationDate();
	}

	@Override
	public String getUpdateMessage() {
		return getAppUpdateModel().getMessages().getCurrentVersionMessage();
	}

	@Override
	public String getMinimumVersion() {
		if (getAppUpdateModel() != null) {
			return getAppUpdateModel().getVersion().getMinimumVersion();
		}
		return null;
	}

	@Override
	public String getMinimumOSverion() {
		return getAppUpdateModel().getRequirements().getMinimumOSVersion();
	}

	private boolean compareVersion(String appVer, String cloudVer) {
		String[] arr1 = appVer.split("\\.");
		String[] arr2 = cloudVer.split("\\.");

		int i = 0;
		while (i < arr1.length || i < arr2.length) {
			if (i < arr1.length && i < arr2.length) {
				if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
					return true;
				} else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
					return false;
				}
			} else if (i < arr1.length) {
				if (Integer.parseInt(arr1[i]) != 0) {
					return false;
				}
			} else if (i < arr2.length) {
				if (Integer.parseInt(arr2[i]) != 0) {
					return true;
				}
			}

			i++;
		}

		return false;
	}
}
