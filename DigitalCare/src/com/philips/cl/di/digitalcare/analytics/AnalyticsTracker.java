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
 * AnalyticsTracker class is responsible for Adobe Analytics. Here all APIs has
 * been added to call for page/event tagging.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Mar 25, 2015
 */
public class AnalyticsTracker {

	public static String TAG = "DigitalCare:Analytics";
	private static boolean trackMetrics = false;

	/**
	 * Analytics: Tagging can be enabled disabled.
	 */
	public static void isEnable(boolean enable) {
		trackMetrics = enable;
	}

	/**
	 * Analytics: Initialize with the context. After initialization only
	 * TAGGIN'S APIs can be used.
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

	/**
	 * Tracking action for events.
	 * 
	 * @param actionName
	 *            : Name of the action.
	 * @param mapKey
	 *            : Key field in the Map.
	 * @param mapValue
	 *            : Value field in the Map.
	 */
	public static void trackAction(String actionName, String mapKey,
			String mapValue) {
		if (!trackMetrics)
			return;
		DLog.i(TAG, "TrackAction : actionName : " + actionName);
		Analytics.trackAction(AnalyticsConstants.ACTION_KEY_SET_ERROR,
				getContextData(mapKey, mapValue));
	}

	private static Map<String, Object> getContextData(String mapKey,
			String mapValue) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(AnalyticsConstants.ACTION_KEY_TIME_STAMP,
				getTimestamp());
		contextData.put(mapKey, mapValue);
		return contextData;
	}

	/*
	 * This context data will be called for every page track.
	 */
	private static Map<String, Object> addAnalyticsDataObject() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(AnalyticsConstants.ACTION_KEY_APPNAME,
				AnalyticsConstants.ACTION_VALUE_APPNAME);
		contextData.put(AnalyticsConstants.ACTION_KEY_VERSION,
				DigitalCareConfigManager.getAppVersion());
		contextData
				.put(AnalyticsConstants.ACTION_KEY_OS,
						AnalyticsConstants.ACTION_VALUE_ANDROID
								+ Build.VERSION.RELEASE);
		contextData.put(AnalyticsConstants.ACTION_KEY_COUNTRY,
				DigitalCareConfigManager.getCountry());
		contextData.put(AnalyticsConstants.ACTION_KEY_LANGUAGE,
				DigitalCareConfigManager.getLanguage());
		contextData.put(AnalyticsConstants.ACTION_KEY_CURRENCY, getCurrency());
		contextData.put(AnalyticsConstants.ACTION_KEY_TIME_STAMP,
				getTimestamp());
		contextData.put(AnalyticsConstants.ACTION_KEY_APP_ID,
				Analytics.getTrackingIdentifier());
		return contextData;
	}

	private static String getCurrency() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		if (currencyCode == null)
			currencyCode = AnalyticsConstants.ACTION_KEY_CURRENCY;
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
