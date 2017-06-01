/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * A Wrapper class for Application Life Cycle.
 */
public class AppTagging implements AppTaggingInterface {

	public static final String PAGE_NAME = "ailPageName";
	public static final String ACTION_NAME = "ailActionName";
	public static final String ACTION_TAGGING_DATA = "ACTION_TAGGING_DATA";
	public static final String EXTRA_TAGGING_DATA = "TAGGING_DATA";
	private static final String AIL_PRIVACY_CONSENT = "ailPrivacyConsentForSensitiveData";
	private static String prevPage;
	private final AppInfra mAppInfra;
	protected String mComponentID;
	protected String mComponentVersion;
	private String mLanguage;
	private Locale mLocale;

	public AppTagging(AppInfra aAppInfra) {
		mAppInfra = aAppInfra;
		init(mAppInfra.getInternationalization().getUILocale(), mAppInfra.getAppInfraContext());
		// Class shall not presume appInfra to be completely initialized at this point.
		// At any call after the constructor, appInfra can be presumed to be complete.
	}


    /*
    * Checks for SSL connection value from Adobe json
    * */

	private boolean checkForSslConnection() {
		boolean sslValue = false;
		final JSONObject jSONObject = getMasterADBMobileConfig();
		try {
			if (jSONObject != null) {
				sslValue = jSONObject.getJSONObject("analytics").optBoolean("ssl");
				if (sslValue) {
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "ssl value",
							"true");
					return sslValue;
				} else {
					if (!checkForProductionState())
						throw new AssertionError("ssl value in ADBMobileConfig.json should be true");
				}
			}
		} catch (JSONException e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AdobeMobile Configuration exception",
					Log.getStackTraceString(e));
		}

		return sslValue;
	}

	private void init(Locale locale, Context context) {
		mLocale = locale;
		Config.setContext(context);
	}


	/**
	 * Reading from Adobe json
	 */
	protected JSONObject getMasterADBMobileConfig() {
		JSONObject result = null;
		try {
			final InputStream mInputStream = mAppInfra.getAppInfraContext().getAssets().open("ADBMobileConfig.json");
			final BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
			final StringBuilder mStringBuilder = new StringBuilder();
			String line;
			while ((line = mBufferedReader.readLine()) != null) {
				mStringBuilder.append(line).append('\n');
			}
			result = new JSONObject(mStringBuilder.toString());
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, "Json",
					result.toString());
		} catch (Exception e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "Tagging ADBMobileConfig file reading exception",
					Log.getStackTraceString(e));
		}
		return result;
	}

	/**
	 * Constructing default Object data
	 **/

	private Map<String, Object> addAnalyticsDataObject() {
		final Map<String, Object> contextData = new HashMap<>();

		contextData.put(AppTaggingConstants.LANGUAGE_KEY, getLanguage());

		contextData.put(AppTaggingConstants.APPSID_KEY, getTrackingIdentifier());
		if (getComponentId() != null) {
			contextData.put(AppTaggingConstants.COMPONENT_ID, getComponentId());
		}
		if (getComponentVersionVersionValue() != null) {
			contextData.put(AppTaggingConstants.COMPONENT_VERSION, getComponentVersionVersionValue());
		}
		contextData.put(AppTaggingConstants.LOCAL_TIMESTAMP_KEY, getLocalTimestamp());
		contextData.put(AppTaggingConstants.UTC_TIMESTAMP_KEY, getUTCTimestamp());
		contextData.put(AppTaggingConstants.BUNDLE_ID, getAppStateFromConfig());

		// Removes Sensitive data from Context object taking Consent

		return removeSensitiveData(contextData);
	}

	/**
	 * Removes Sensitive data declared in Adobe json.
	 **/

	private Map<String, Object> removeSensitiveData(Map<String, Object> data) {

		final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
				.AppConfigurationError();
		if (getPrivacyConsentForSensitiveData() && mAppInfra.getConfigInterface() != null) {
			try {
				final Object object = mAppInfra.getConfigInterface().getPropertyForKey
						("tagging.sensitiveData", "appinfra", configError);
				if (object instanceof ArrayList<?>) {
					final ArrayList<?> taggingSensitiveData = (ArrayList<?>) object;
					if (taggingSensitiveData.size() > 0) {
						data.keySet().removeAll(taggingSensitiveData);
					}
				}
			} catch (Exception e) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
						"Tagging", "" + e);
			}
		}
		return data;

	}

	@Override
	public String getTrackingIdentifier() {
		return Analytics.getTrackingIdentifier();
	}



	private String getAppStateFromConfig() {

		if (mAppInfra.getAppIdentity() != null) {
			try {
				return mAppInfra.getAppIdentity().
						getAppState().toString();
			} catch (Exception e) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
						"Tagging", "" + e);
			}
		}

		return null;
	}

	private String getLanguage() {
		if (mLanguage == null) {
			mLanguage = mLocale.getLanguage();
		}
		return mLanguage;

	}

	private String getUTCTimestamp() {
		String mUTCTimestamp = null;

		if (mAppInfra.getTime() != null) {
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
			dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));

			mUTCTimestamp = dateFormat.format(mAppInfra.getTime().getUTCTime());
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
					"Tagging", mUTCTimestamp);

		}

		return mUTCTimestamp;
	}

	private String getLocalTimestamp() {
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", Locale.ENGLISH);
		final String mLocalTimestamp = dateFormat.format(calendar.getTime());
		return mLocalTimestamp;
	}

	private String getComponentId() {
		return mComponentID;
	}

	private String getComponentVersionVersionValue() {
		return mComponentVersion;
	}

	@Override
	public AppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
		return new AppTaggingWrapper(mAppInfra, componentId, componentVersion);
	}

	@Override
	public void setPreviousPage(String previousPage) {
		prevPage = previousPage;
	}

	@Override
	public PrivacyStatus getPrivacyConsent() {
		final MobilePrivacyStatus mMobilePrivacyStatus = Config.getPrivacyStatus();
		PrivacyStatus mPrivacyStatus = null;
		switch (mMobilePrivacyStatus) {
			case MOBILE_PRIVACY_STATUS_OPT_IN:
				mPrivacyStatus = PrivacyStatus.OPTIN;
				break;
			case MOBILE_PRIVACY_STATUS_OPT_OUT:
				mPrivacyStatus = PrivacyStatus.OPTOUT;
				break;
			case MOBILE_PRIVACY_STATUS_UNKNOWN:
				mPrivacyStatus = PrivacyStatus.UNKNOWN;
				break;
		}
		return mPrivacyStatus;
	}

	@Override
	public void setPrivacyConsent(PrivacyStatus privacyStatus) {
		switch (privacyStatus) {
			case OPTIN:
				Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);
				break;
			case OPTOUT:
				Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);

				break;
			case UNKNOWN:
				Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN);
				break;

		}
	}

	private void track(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
		if (checkForSslConnection() || checkForProductionState()) {
			trackData(pageName, paramMap, isTrackPage);
		}
	}

	private boolean checkForProductionState() {

		if (mAppInfra.getAppIdentity() != null) {
			try {
				return !mAppInfra.getAppIdentity().
						getAppState().toString().equalsIgnoreCase("Production");
			} catch (Exception e) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
						"Tagging", "" + e);
			}
		}

		return false;
	}

	private void trackData(String pageName, Map<String, String> paramMap, boolean isTrackPage) {
		boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
		Map contextData = addAnalyticsDataObject();
		if (paramMap != null) {
			paramMap.putAll(contextData);
			contextData = removeSensitiveData((HashMap) paramMap);
		}

		if (null != prevPage && isTrackPage) {
			contextData.put(AppTaggingConstants.PREVIOUS_PAGE_NAME, prevPage);
		}
		if (isTrackPage) {
			if(pageName!=null &&  !pageName.isEmpty()){

				if(pageName.getBytes().length>100)
				{
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Page name exceeds 100 bytes in length");
					/*if(isDebuggable) {
						if (!checkForProductionState()) {
							mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Page name exceeds 100 bytes in length");
						} else {
							throw new IllegalArgumentException("App Tagging," + " Page name exceeds 100 bytes in length");
						}
					}*/

				}
				if(pageName.equalsIgnoreCase(prevPage)){
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Page name and previous page name shouldn't be same");
					/*if(isDebuggable) {
						if (!checkForProductionState()) {
							mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Page name and previous page name shouldn't be same");
						} else {
							throw new IllegalArgumentException("App Tagging," + " Page name and previous page name shouldn't be same");
						}
					}*/
				}
				Analytics.trackState(pageName, contextData);
			}
			else
			{
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "App Tagging","Page name should not  be empty ");
			}

			contextData.put(PAGE_NAME, pageName);
			prevPage = pageName;
		} else {
			String event=pageName.replaceAll("\\s+","");
			if(event!=null &&  !event.isEmpty()){

				if(event.getBytes().length>255)
				{
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Event  exceeds 255 bytes in length");
					/*if(isDebuggable) {
						if (!checkForProductionState()) {
							mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "App Tagging", "Event  exceeds 255 bytes in length");
						} else {
							throw new IllegalArgumentException("App Tagging," + " Event  exceeds 255 bytes in length");
						}
					}*/

				}

				Analytics.trackAction(event, contextData);

			}
			else
			{
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "App Tagging","Event  is null ");
			}
			contextData.put(ACTION_NAME ,event);
		}

		sendBroadcast(contextData);  // sending broadcast
	}

	@Override
	public void trackTimedActionStart(String actionStart) {

		if (checkForSslConnection() || checkForProductionState()) {
			Analytics.trackTimedActionStart(actionStart, addAnalyticsDataObject());
		}
	}

	@Override
	public void trackTimedActionEnd(String actionEnd) {
		if (checkForSslConnection() || checkForProductionState()) {
			Analytics.trackTimedActionEnd(actionEnd, null);
		}
	}

	@Override
	public boolean getPrivacyConsentForSensitiveData() {
		final String consentValueString = mAppInfra.getSecureStorage().fetchValueForKey(AIL_PRIVACY_CONSENT, getSecureStorageErrorValue());
		final boolean consentValue = consentValueString != null && consentValueString.equalsIgnoreCase("true");
		mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,
				"Tagging-consentValue", "" + consentValue);
		return consentValue;
	}

	// Sets the value of Privacy Consent For Sensitive Data and stores in preferences
	@Override
	public void setPrivacyConsentForSensitiveData(boolean valueContent) {
		mAppInfra.getSecureStorage().storeValueForKey(AIL_PRIVACY_CONSENT, String.valueOf(valueContent), getSecureStorageErrorValue());
	}

	private SecureStorage.SecureStorageError getSecureStorageErrorValue() {
		return new SecureStorage.SecureStorageError();
	}


	@Override
	public void trackPageWithInfo(String pageName, String key, String value) {
		trackWithInfo(pageName, key, value, true);
	}

	@Override
	public void trackPageWithInfo(String pageName, Map<String, String> paramMap) {
		track(pageName, paramMap, true);
	}


	@Override
	public void trackActionWithInfo(String pageName, String key, String value) {
		trackWithInfo(pageName, key, value, false);
	}

	private void trackWithInfo(String pageName, String key, String value, boolean isTrackPage) {
		final HashMap<String, String> trackMap = new HashMap<>();
		trackMap.put(key, value);
		track(pageName, trackMap, isTrackPage);
	}

	@Override
	public void trackActionWithInfo(String pageName, Map<String, String> paramMap) {
		track(pageName, paramMap, false);
	}

	@Override
	public void collectLifecycleInfo(Activity context, Map<String, Object> paramDict) {
		Config.collectLifecycleData(context, paramDict);
	}

	@Override
	public void collectLifecycleInfo(Activity context) {
		Config.collectLifecycleData(context);
	}

	@Override
	public void pauseLifecycleInfo() {
		Config.pauseCollectingLifecycleData();
	}

	@Override
	public void trackVideoStart(String videoName) {
		trackActionWithInfo("videoStart", "videoName", videoName);
	}

	@Override
	public void trackVideoEnd(String videoName) {
		trackActionWithInfo("videoEnd", "videoName", videoName);
	}

	@Override
	public void trackSocialSharing(SocialMedium medium, String sharedItem) {
		final HashMap<String, String> trackMap = new HashMap<>();
		trackMap.put("socialItem", sharedItem);
		trackMap.put("socialType", medium.toString());
		trackActionWithInfo("socialShare", trackMap);
	}

	@Override
	public void trackLinkExternal(String url) {
		trackActionWithInfo("sendData", "exitLinkName", url);
	}

	@Override
	public void trackFileDownload(String filename) {
		trackActionWithInfo("sendData", "fileName", filename);
	}

	/**
	 * Sending the broadcast event .
	 * @param data Map consists of Tagging Data
	 */
	private void sendBroadcast(final Map data) {
		Intent intent = new Intent(ACTION_TAGGING_DATA);
		intent.putExtra(EXTRA_TAGGING_DATA, (Serializable) data);
		LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
				.sendBroadcast(intent);
	}

	@Override
	public void unregisterTaggingData(final BroadcastReceiver receiver) {
		if(receiver != null && mAppInfra.getAppInfraContext() != null)  {
			LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
					.unregisterReceiver(receiver);
		} else {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
					"unregisterTaggingData", "" + "context is null");
		}
	}

	@Override
	public void registerTaggingData(final BroadcastReceiver receiver) {
		if(receiver != null && mAppInfra.getAppInfraContext() != null)  {
			LocalBroadcastManager.getInstance(mAppInfra.getAppInfraContext())
					.registerReceiver(receiver, new IntentFilter(ACTION_TAGGING_DATA));
		} else {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
					"unregisterTaggingData", "" + "context is null");
		}
	}

}
