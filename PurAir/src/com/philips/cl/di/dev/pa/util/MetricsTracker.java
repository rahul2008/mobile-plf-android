package com.philips.cl.di.dev.pa.util;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.philips.cl.di.dev.pa.PurAirApplication;

public class MetricsTracker {

	private static String DEFAULT_COUNTRY = "NL";
	private static String DEFAULT_LANGUAGE = "en";
	private static String DEFAULT_CURRENCY = "EUR";

	private static final String KEY_CP = "sector";
	private static final String KEY_APPNAME = "app.name";
	private static final String KEY_VERSION = "app.version";
	private static final String KEY_OS = "app.os";
	private static final String KEY_COUNTRY = "locale.country";
	private static final String KEY_LANGUAGE = "locale.language";
	private static final String KEY_CURRENCY = "locale.currency";
	private static final String KEY_FILENAME = "fileName";
	private static final String KEY_EXIT_LINK = "exitLinkName";
	private static final String KEY_ERROR_USER = "userError";
	private static final String KEY_ERROR_TECHNICAL = "technicalerror";
	private static final String KEY_PAGE_EVENT = "pageEvents";
	private static final String KEY_LOCATION_PURIFIER = "newPurifyLocation";
	private static final String KEY_LOCATION_WEATHER = "newPurifyLocation";
	private static final String KEY_OPTION_DETAILS = "optionDetail";
	// private static final String KEY_POWER_ON = "optionDetail";
	// private static final String KEY_POWER_ON = "optionDetail";

	private static final String ACTION_EXIT_LINK = "exit link";
	private static final String ACTION_DOWNLOAD = "download";
	private static final String ACTION_ERROR_SET = "setError";
	private static final String ACTION_LOCATION_NEW_PURIFIER = "newPurifierRequest";
	private static final String ACTION_LOCATION_NEW_WEATHER = "newWeatherRequest";
	private static final String ACTION_SET_OPTION = "setOption";
	// private static final String ACTION_POWER_SET_OPTION = "setOption";
	// private static final String ACTION_POWER_SET_OPTION = "setOption";

	private static String VALUE_CP = "CP";
	private static String VALUE_APPNAME = "PurAir";

	public static void initContext(Context context) {
		Config.setContext(context);
	}

	public static void startCollectLifecycleData(Activity activity) {
		Config.collectLifecycleData(activity);
	}

	public static void stopCollectLifecycleData() {
		Config.pauseCollectingLifecycleData();
	}

	/*
	 * This has to be tracked by each page.
	 */
	public static void trackPage(String pageName) {
		Analytics.trackState(pageName, addAnalyticsDataObject());
	}

	/*
	 * "Fill the NSDictonairy DATAOBJ with as property userError, and with
	 * NSString the errors that are visible on the screen. This NSDictonairy is
	 * used when sending the pagename. If there is no new page the error occurs
	 * on (for instance a check is done when pressing a button and no new page
	 * is displayed with the error but instead the error occurs inline, use a
	 * trackAction method to send the DATAOBJ. "
	 */
	public static void trackActionUserError() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		contextData.put(KEY_ERROR_USER, "incorrect e-mail address");
		Analytics.trackAction(ACTION_ERROR_SET, contextData);
	}

	/*
	 * "Fill the NSDictonairy DATAOBJ with as property technicalError, and with
	 * NSString the technical errors. This NSDictonairy is used when sending the
	 * pagename. If there is no new page the error occurs on (for instance a
	 * check is done when pressing a button and no new page is displayed with
	 * the error but instead the error occurs inline, use a trackAction method
	 * to send the DATAOBJ."
	 */
	public static void trackActionTechnicalError() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		contextData.put(KEY_ERROR_TECHNICAL,
				"we're having trouble connecting to your Air Purifier");
		Analytics.trackAction(ACTION_ERROR_SET, contextData);
	}

	/*
	 * When the app is left for a different activity or app. Note that this is
	 * triggered by a link/button/functionality of the app. Do not use this when
	 * the visitor swiches using the home button.
	 */
	public static void trackActionExit(String link) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_EXIT_LINK, link);
		Analytics.trackAction(ACTION_EXIT_LINK, contextData);
	}

	/*
	 * When a file is downloaded.
	 */
	public static void trackActionDownloaded(String fileName) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_FILENAME, fileName);
		Analytics.trackAction(ACTION_DOWNLOAD, contextData);
	}

	/*
	 * Page where the registration starts for a machine.
	 */
	public static void trackActionStartUserRegistration() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		contextData.put(KEY_PAGE_EVENT, "startProductRegistration");
		// Analytics.trackAction(ACTION_DOWNLOAD, contextData);
	}

	/*
	 * Page where the product regristration is finished (product regristration
	 * confirmation page).
	 */
	public static void trackActionFinishedUserRegistration() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		contextData.put(KEY_PAGE_EVENT, "successProductRegistration");
		// Analytics.trackAction(ACTION_DOWNLOAD, contextData);
	}

	/*
	 * When a purifier location is successfully added.
	 */
	public static void trackActionLocationPurifier(String location) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_LOCATION_PURIFIER, location);
		Analytics.trackAction(ACTION_LOCATION_NEW_PURIFIER, contextData);
	}

	/*
	 * When a weather (air quality) location is successfully added.
	 */
	public static void trackActionLocationWeather(String location) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_LOCATION_WEATHER, location);
		Analytics.trackAction(ACTION_LOCATION_NEW_WEATHER, contextData);
	}

	/*
	 * Toggle power
	 */
	public static void trackActionTogglePower(Boolean toggle) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (toggle) {
			contextData.put(KEY_OPTION_DETAILS, "power_on");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "power_off");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When the purifiers light setting is changed
	 */
	public static void trackActionIndicatorLight(Boolean light) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (light) {
			contextData.put(KEY_OPTION_DETAILS, "indicator_light_on");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "indicator_light_off");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When the purifier fan speed is changed.
	 */
	public static void trackActionFanSpeed(String speed) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_OPTION_DETAILS, speed);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When the purifiers child lock setting is changed
	 */
	public static void trackActionChildLock(Boolean lock) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (lock) {
			contextData.put(KEY_OPTION_DETAILS, "childlock_on");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "childlock_off");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When notifications are enabled/disabled through the app
	 */
	public static void trackActionNotification(Boolean notification) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (notification) {
			contextData.put(KEY_OPTION_DETAILS, "notification_on");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "notification_off");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When remote control is enabled/disabled through the app
	 */
	public static void trackActionRemoteControl(Boolean remote) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (remote) {
			contextData.put(KEY_OPTION_DETAILS, "remote_control_on");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "remote_control_off");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * Add parameter to connection process completion tracking and to error
	 * tracking of connection process.
	 */
	public static void trackActionAdvanceNetworkConfig(Boolean config) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		if (config) {
			contextData.put(KEY_OPTION_DETAILS, "advance_network:yes");
		} else {
			contextData.put(KEY_OPTION_DETAILS, "advance_network:no");
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	/*
	 * When the purifier fan speed is changed.
	 */
	public static void trackActionTFanSpeed(String speed) {
		Map<String, Object> contextData = new HashMap<String, Object>();
		// TODO : Strings has to be added to xml file.
		contextData.put(KEY_OPTION_DETAILS, speed);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	private static Map<String, Object> addAnalyticsDataObject() {
		System.out.println("ADBMobile.addAnalyticsDataObject()");
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_CP, VALUE_CP);
		contextData.put(KEY_APPNAME, VALUE_APPNAME);
		contextData.put(KEY_VERSION, PurAirApplication.getAppVersion());
		contextData.put(KEY_OS, "Android " + Build.VERSION.RELEASE);
		contextData.put(KEY_COUNTRY, getCountry());
		contextData.put(KEY_LANGUAGE, getLanguage());
		contextData.put(KEY_CURRENCY, getCurrency());
		return contextData;
	}

	private static String getCountry() {
		String country = PurAirApplication.getAppContext().getResources()
				.getConfiguration().locale.getCountry().toLowerCase(
				Locale.getDefault());
		if (country == null)
			country = DEFAULT_COUNTRY;
		return country;
	}

	private static String getLanguage() {
		String language = PurAirApplication.getAppContext().getResources()
				.getConfiguration().locale.getLanguage();
		if (language == null)
			language = DEFAULT_LANGUAGE;
		return language;
	}

	private static String getCurrency() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		if (currencyCode == null)
			currencyCode = DEFAULT_CURRENCY;
		return currencyCode;
	}

}
