package com.philips.cl.di.digitalcare.analytics;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * 
 * AnalyticsTracker class is responsible for Adobe Analytics. Here all APIs has
 * been added to call for page/event tagging.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Mar 25, 2015
 */
public class AnalyticsTracker implements AnalyticsConstants {

	private static boolean trackMetrics = false;

	/**
	 * Analytics: Tagging can be enabled disabled.
	 */
	public static void isEnable(boolean enable) {
		trackMetrics = enable;
	}

	/**
	 * Analytics: Initialize with the context. After initialization only TAGGIN'S
	 * APIs can be used.
	 */
	public static void initContext(Context context) {
		if (!trackMetrics)
			return;
		Config.setContext(context);
	}

	/**
	 * Analytics: This needs to call on onResume() of every activity.
	 * 
	 * @param activity
	 */
	public static void startCollectLifecycleData() {
		if (!trackMetrics)
			return;
		// TODO : Create thread.
		Config.collectLifecycleData();
	}

	/*
	 * Analytics: This needs to call on onPause() of every activity.
	 */
	public static void stopCollectLifecycleData() {
		if (!trackMetrics)
			return;
		// TODO : Create thread.
		Config.pauseCollectingLifecycleData();
	}

	public static void trackPage(String pageName) {
		if (!trackMetrics)
			return;
		DLog.i(TAG, "Track page :" + pageName);
		Analytics.trackState(pageName, addAnalyticsDataObject());
	}

	public static void trackAction(String serviceChannel, String key,
			String value) {
		if (!trackMetrics)
			return;
		DLog.i(TAG, "TrackAction : ServiceRequest : " + serviceChannel);
		// Map<String, Object> contextData = new HashMap<String, Object>();
		// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
		// contextData.put(ACTION_KEY_USER_ERROR, errorMsg);
		Analytics.trackAction(ACTION_KEY_SET_ERROR, getContextData(key, value));
	}

	private static Map<String, Object> getContextData(String key, String value) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
		contextData.put(key, value);
		return contextData;
	}

	// public static void trackPageTechnicalError(String pageName, String
	// errorMsg) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackPage : TechnicalError " + errorMsg);
	// Map<String, Object> contextData = addAnalyticsDataObject();
	// contextData.put(ACTION_KEY_TECHNICAL_ERROR, errorMsg);
	// Analytics.trackState(pageName, contextData);
	// }
	//
	// public static void trackPageUserError(String pageName, String errorMsg) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackPage : UserError " + errorMsg);
	// Map<String, Object> contextData = addAnalyticsDataObject();
	// contextData.put(ACTION_KEY_USER_ERROR, errorMsg);
	// Analytics.trackState(pageName, contextData);
	// }
	//
	// // public static void trackPageUserLoginChannel(String loginChannel) {
	// // if (!trackMetrics)
	// // return;
	// // DLog.i(TAG, "TrackPage : loginChannel : " + loginChannel);
	// // Map<String, Object> contextData = addAnalyticsDataObject();
	// // contextData.put(ACTION_ACTION_KEY_LOGIN_CHANNEL, loginChannel);
	// // Analytics.trackState(ACTION_ACTION_KEY_PAGE_USER_LOGIN, contextData);
	// // }
	//
	// public static void trackUserError(String action, String errorMsg) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackUserError : action " + action + " errorMessage "
	// + errorMsg);
	// Map<String, Object> contextData = addAnalyticsDataObject();
	// contextData.put(ACTION_KEY_PAGE_EVENT, ACTION_VALUE_START_CONNECTION);
	// }
	//
	// public static void trackActionServiceRequest(String serviceChannel) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : ServiceRequest : " + serviceChannel);
	// Map<String, Object> contextData = addAnalyticsDataObject();
	// contextData.put("serviceChannel", serviceChannel);
	// Analytics.trackAction("serviceRequest", contextData);
	// }
	//
	// // public static void trackPageStartUserRegistration(String
	// // registrationChannel) {
	// // if (!trackMetrics)
	// // return;
	// // // @argument: registration channel means facebook, twitter etc.
	// // DLog.i(TAG, "TrackState : StartUserRegistration : channel "
	// // + registrationChannel);
	// // Map<String, Object> contextData = addAnalyticsDataObject();
	// // contextData.put(ACTION_KEY_PAGE_EVENT, VALUE_START_USER_REGISTRATION);
	// // contextData.put(ACTION_KEY_REGISTRATION_CHANNEL, registrationChannel);
	// // Analytics.trackState(PAGE_USER_REGISTRATION, contextData);
	// // }
	// //
	// // public static void trackPageFinishedUserRegistration() {
	// // if (!trackMetrics)
	// // return;
	// // DLog.i(TAG, "TrackPage : user registration successful");
	// // Map<String, Object> contextData = addAnalyticsDataObject();
	// // contextData.put(ACTION_KEY_PAGE_EVENT, ACTION_VALUE_SUCCESS_USER_REG);
	// // Analytics.trackState(ACTION_KEY_USER_REGISTRATION, contextData);
	// // }
	//
	// // public static void trackPageSuccessLoginUser(String pageName) {
	// // if (!trackMetrics)
	// // return;
	// // DLog.i(TAG, "TrackPage : SuccessLogin " + pageName);
	// // Map<String, Object> contextData = addAnalyticsDataObject();
	// // contextData.put(ACTION_KEY_PAGE_EVENT, ACTION_VALUE_SUCCESS_LOGIN);
	// // Analytics.trackState((pageName == null) ? PAGE_USER_REGISTRATION
	// // : pageName, contextData);
	// // }
	//
	// //
	// // public static void trackPageProductView(String products) {
	// // if (!trackMetrics)
	// // return;
	// // DLog.i(TAG, "TrackPage : PageProductView " + products);
	// // Map<String, Object> contextData = addAnalyticsDataObject();
	// // contextData.put(ACTION_KEY_PAGE_EVENT, ACTION_VALUE_PRODUCT_VIEW);
	// // Analytics.trackState(products, contextData);
	// // }
	//
	// public static void trackActionUserError(String errorMsg) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : UserError " + errorMsg);
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_USER_ERROR, errorMsg);
	// Analytics.trackAction(ACTION_KEY_SET_ERROR, contextData);
	// }
	//
	// public static void trackActionTechnicalError(String errorMsg) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : TechnicalError " + errorMsg);
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_TECHNICAL_ERROR, errorMsg);
	// Analytics.trackAction(ACTION_KEY_SET_ERROR, contextData);
	// }
	//
	// public static void trackActionAppStatus(String appStatus) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : AppStatus " + appStatus);
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_APP_STATUS, appStatus);
	// Analytics.trackAction(ACTION_KEY_SET_APP_STATUS, contextData);
	// }
	//
	// /*
	// * When the app is left for a different activity or app. Note that this is
	// * triggered by a link/button/functionality of the app. Do not use this
	// when
	// * the visitor swiches using the home button.
	// */
	// public static void trackActionExitLink(String link) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : ExitLink " + link);
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_EXIT_LINK, link);
	// Analytics.trackAction(ACTION_KEY_EXIT_LINK, contextData);
	// }
	//
	// public static void trackActionBuyButton() {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : BuyButton");
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put("leadinfo", "Philips lead");
	// Analytics.trackAction("buyButton", contextData);
	// }
	//
	// public static void trackActionInAppNotification(String message) {
	// DLog.i(TAG, "TrackAction : InAppNotification");
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put("messageValue", message);
	// Analytics.trackAction("popupMessage", contextData);
	// }
	//
	// public static void trackActionInAppNotificationPositiveResponse(
	// String message) {
	// DLog.i(TAG, "TrackAction : InAppNotification");
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put("messageValue", message);
	// Analytics.trackAction("acceptMessage", contextData);
	// }
	//
	// public static void trackActionDownloaded(String fileName) {
	// if (!trackMetrics)
	// return;
	// DLog.i(TAG, "TrackAction : DownloadFile " + fileName);
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_FILE_NAME, fileName);
	// Analytics.trackAction(ACTION_KEY_DOWNLOAD, contextData);
	// }
	//
	// public static void trackActionVideoStart(String videoName) {
	// if (!trackMetrics)
	// return;
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_VIDEO_NAME, videoName);
	// Analytics.trackAction(ACTION_KEY_VIDEO_START, contextData);
	// }
	//
	// public static void trackActionVideoEnd(String videoName) {
	// if (!trackMetrics)
	// return;
	// Map<String, Object> contextData = new HashMap<String, Object>();
	// contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
	// contextData.put(ACTION_KEY_VIDEO_NAME, videoName);
	// Analytics.trackAction(ACTION_KEY_VIDEO_END, contextData);
	// }

	/*
	 * This context data will be called for every page track.
	 */
	private static Map<String, Object> addAnalyticsDataObject() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(ACTION_KEY_APPNAME, ACTION_VALUE_APPNAME);
		contextData.put(ACTION_KEY_VERSION,
				DigitalCareConfigManager.getAppVersion());
		contextData.put(ACTION_KEY_OS, ACTION_VALUE_ANDROID
				+ Build.VERSION.RELEASE);
		contextData.put(ACTION_KEY_COUNTRY,
				DigitalCareConfigManager.getCountry());
		contextData.put(ACTION_KEY_LANGUAGE,
				DigitalCareConfigManager.getLanguage());
		contextData.put(ACTION_KEY_CURRENCY, getCurrency());
		contextData.put(ACTION_KEY_TIME_STAMP, getTimestamp());
		contextData.put(ACTION_KEY_APP_ID, Analytics.getTrackingIdentifier());
		return contextData;
	}

	private static String getCurrency() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		if (currencyCode == null)
			currencyCode = ACTION_KEY_CURRENCY;
		return currencyCode;
	}

	@SuppressLint("SimpleDateFormat")
	private static String getTimestamp() {
		long timeMillis = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date(timeMillis));
		return date;
	}
}
