package com.philips.cl.di.dev.pa.util;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Analytics.TimedActionBlock;
import com.adobe.mobile.Config;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;

public class MetricsTracker {

	/* ---------------DEFAULT LIST ----------------------- */
	private static final String DEFAULT_LANGUAGE = "en";
	private static final String DEFAULT_CURRENCY = "EUR";

	/* ---------------KEY LIST ----------------------- */
	private static final String KEY_APPNAME = "app.name";
	private static final String KEY_VERSION = "app.version";
	private static final String KEY_OS = "app.os";
	private static final String KEY_LANGUAGE = "locale.language";
	private static final String KEY_CURRENCY = "locale.currency";
    private static final String KEY_TIMESTAMP = "timestamp";
	private static final String KEY_FILENAME = "fileName";
	private static final String KEY_EXIT_LINK = "exitLinkName";
	private static final String KEY_ERROR_USER = "userError";
	private static final String KEY_ERROR_TECHNICAL = "technicalerror";
	private static final String KEY_PAGE_EVENT = "pageEvents";
	private static final String KEY_LOCATION_PURIFIER = "newPurifyLocation";
	private static final String KEY_LOCATION_WEATHER = "newWeatherLocation";
	private static final String KEY_OPTION_DETAILS = "optionDetail";
	private static final String KEY_VIDEO_NAME = "videoName";
	private static final String KEY_FIRMWARE_VERSION = "firmwareVersion";
	private static final String KEY_PRODUCT_MODEL = "productModel";
	private static final String KEY_APP_ID = "appsId";
	private static final String KEY_LOGIN_CHANNEL = "loginChannel";
	private static final String KEY_REGISTRATION_CHANNEL = "registrationChannel";
	private static final String KEY_APP_STATUS = "appStatus";
	private static final String KEY_CONTROL_CONNECTION_TYPE = "controlConnectionType";
	private static final String KEY_MACHINE_ID = "machineId";

	/* ---------------ACTION LIST ----------------------- */
	private static final String ACTION_VIDEO_START = "videoStart";
	private static final String ACTION_VIDEO_END = "videoEnd";
	private static final String ACTION_EXIT_LINK = "exit link";
	private static final String ACTION_DOWNLOAD = "download";
	private static final String ACTION_ERROR_SET = "setError";
	private static final String ACTION_LOCATION_NEW_PURIFIER = "newPurifierRequest";
	private static final String ACTION_LOCATION_NEW_WEATHER = "newWeatherRequest";
	private static final String ACTION_SET_OPTION = "setOption";
	private static final String ACTION_SET_APP_STATUS = "setAppStatus";
	private static final String ACTION_SET_CONTROL_CONNECTION_TYPE = "setControl";

	/*----------------PAGE LIST------------------------*/
	private static final String PAGE_USER_REGISTRATION = "UserRegistration";
	private static final String PAGE_USER_LOGIN = "UserLogin";

	/* ---------------VALUE LIST ----------------------- */
	private static final String VALUE_APPNAME = "PurAir";
	private static final String ANDROID = "Android ";
	private static final String VALUE_ADVANCE_NETWORK_NO = "advance_network:no";
	private static final String VALUE_ADVANCE_NETWORK_YES = "advance_network:yes";
	private static final String VALUE_REMOTE_CONTROL_OFF = "remote control off";
	private static final String VALUE_REMOTE_CONTROL_ON = "remote control on";
	private static final String VALUE_NOTIFICATION_OFF = "Remote Notification off";
	private static final String VALUE_NOTIFICATION_ON = "Remote Notification on";
	private static final String VALUE_SPEED = "FanSpeed ";
	private static final String VALUE_NOTIFICATION_AIR_QUALITY = "notification air quality:";
	private static final String VALUE_TIMER = "Timer ";
	private static final String VALUE_SCHEDULE_ADDED = "schedule added";
	private static final String VALUE_SUCCESS_USER_REGISTRATION = "successUserRegistration";
	private static final String VALUE_START_USER_REGISTRATION = "startUserRegistration";
//	private static final String VALUE_MY_PHILIPS = "myPhilips";
	private static final String VALUE_SUCCESS_LOGIN = "successLogin";
	private static final String VALUE_PRODUCT_VIEW = "prodView";
	private static final String VALUE_MODEL_AC4373 = "AC4373";
	
	private static boolean trackMetrics = true;
	
	public static void enableTagging() {
		trackMetrics = true;
	}
	
	public static void disableTagging() {
		trackMetrics = false;
	}

	public static void initContext(Context context) {
		if(!trackMetrics) return;
		Config.setContext(context);
	}

	// This needs to be called on onResume() of every activity.
	public static void startCollectLifecycleData(Activity activity) {
		if(!trackMetrics) return;
		Config.collectLifecycleData();
	}

	// This needs to be called on onPause() of every activity.
	public static void stopCollectLifecycleData() {
		if(!trackMetrics) return;
		Config.pauseCollectingLifecycleData();
	}

	public static void trackPage(String pageName) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "Track page " + pageName);
		Analytics.trackState(pageName, addAnalyticsDataObject());
	}
	
	public static void trackPageUserLoginChannel(String loginChannel) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : loginChannel : " + loginChannel); 
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_LOGIN_CHANNEL, loginChannel);
		Analytics.trackState(PAGE_USER_LOGIN, contextData);
	}
	
	public static void trackActionEWSStart() {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackTimedAction : EWSStart");
//		Map<String, Object> contextData = addAnalyticsDataObject();
//		contextData.put(KEY_PAGE_EVENT, "startConnection");
		Analytics.trackTimedActionStart("connectionTime", null);
	}
	
	public static void trackActionEWSSuccess() {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackTimedAction : EWSSuccess");
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, "successConnection");
		Analytics.trackTimedActionEnd("connectionTime", new TimedActionBlock<Boolean>() {
			@Override
			public Boolean call(long inAppDuration, long totalDuration, Map<String, Object> contextData) {
				return true;
			}
		});
	}
	
	public static void trackUserError(String action, String errorMsg) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackUserError : action " + action + " errorMessage " + errorMsg);
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, "startConnection");
	}

	public static void trackActionServiceRequest(String serviceChannel) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : ServiceRequest : " + serviceChannel);
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put("serviceChannel", serviceChannel);
		Analytics.trackAction("serviceRequest", contextData);
	}
	
	public static void trackPageStartUserRegistration(String registrationChannel) {
		if(!trackMetrics) return;
		// @argument: registration channel means facebook, twitter etc.
		ALog.i(ALog.TAGGING, "TrackState : StartUserRegistration : channel " + registrationChannel);
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, VALUE_START_USER_REGISTRATION);
		contextData.put(KEY_REGISTRATION_CHANNEL, registrationChannel);
		Analytics.trackState(PAGE_USER_REGISTRATION, contextData);
	}

	public static void trackPageFinishedUserRegistration() {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : user registration successful");
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, VALUE_SUCCESS_USER_REGISTRATION);
		Analytics.trackState(PAGE_USER_REGISTRATION, contextData);
	}

	public static void trackPageSuccessLoginUser(String pageName) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : SuccessLogin " + pageName);
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, VALUE_SUCCESS_LOGIN);
		Analytics.trackState((pageName == null) ? PAGE_USER_REGISTRATION : pageName, contextData);
	}

	public static void trackPageProductView(String products) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : PageProductView " + products);
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(KEY_PAGE_EVENT, VALUE_PRODUCT_VIEW);
		Analytics.trackState(products, contextData);
	}
	
	public static void trackPageUserError(String pageName, String errorMsg) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : UserError " + errorMsg);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_ERROR_USER, errorMsg);
		Analytics.trackState(pageName, contextData);
	}

	public static void trackActionUserError(String errorMsg) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : UserError " + errorMsg);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_ERROR_USER, errorMsg);
		Analytics.trackAction(ACTION_ERROR_SET, contextData);
	}

	public static void trackPageTechnicalError(String pageName, String errorMsg) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackPage : TechnicalError " + errorMsg);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_ERROR_TECHNICAL, errorMsg);
		Analytics.trackState(pageName, contextData);
	}
	
	public static void trackActionTechnicalError(String errorMsg) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : TechnicalError " + errorMsg);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_ERROR_TECHNICAL, errorMsg);
		Analytics.trackAction(ACTION_ERROR_SET, contextData);
	}

	public static void trackActionAppStatus(String appStatus) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : AppStatus " + appStatus);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_APP_STATUS, appStatus);
		Analytics.trackAction(ACTION_SET_APP_STATUS, contextData);
	}

    //TODO : Verify String value
	public static void trackActionConnectionType(ConnectionState connectionType) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : ConnectionType " + connectionType);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_CONTROL_CONNECTION_TYPE, connectionType);
		Analytics.trackAction(ACTION_SET_CONTROL_CONNECTION_TYPE, contextData);
	}

	/*
	 * When the app is left for a different activity or app. Note that this is
	 * triggered by a link/button/functionality of the app. Do not use this when
	 * the visitor swiches using the home button.
	 */
    // TODO : Doesn't match with iOS. Align
	public static void trackActionExitLink(String link) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : ExitLink " + link);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_EXIT_LINK, link);
		Analytics.trackAction(ACTION_EXIT_LINK, contextData);
	}

	public static void trackActionBuyButton(String leadType) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : BuyButton");
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put("leadInfo", leadType);
		Analytics.trackAction("buyButton", contextData);
	}

    public static void trackActionInAppNotification(String message) {
        ALog.i(ALog.TAGGING, "TrackAction : InAppNotification");
        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
        contextData.put("messageValue", message);
        Analytics.trackAction("popupMessage", contextData);
    }

    public static void trackActionInAppNotificationPositiveResponse(String message) {
        ALog.i(ALog.TAGGING, "TrackAction : InAppNotification");
        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
        contextData.put("messageValue", message);
        Analytics.trackAction("acceptMessage", contextData);
    }

	public static void trackActionDownloaded(String fileName) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : DownloadFile " + fileName);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_FILENAME, fileName);
		Analytics.trackAction(ACTION_DOWNLOAD, contextData);
	}

	public static void trackActionLocationPurifier(String location) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : Puirifer location " + location);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_LOCATION_PURIFIER, location);
		Analytics.trackAction(ACTION_LOCATION_NEW_PURIFIER, contextData);
	}

	public static void trackActionLocationWeather(String location) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : weather location " + location);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_LOCATION_WEATHER, location);
		Analytics.trackAction(ACTION_LOCATION_NEW_WEATHER, contextData);
	}

	public static void trackActionVideoStart(String videoName) {
		if(!trackMetrics) return;
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_VIDEO_NAME, videoName);
		Analytics.trackAction(ACTION_VIDEO_START, contextData);
	}

	public static void trackActionVideoEnd(String videoName) {
		if(!trackMetrics) return;
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_VIDEO_NAME, videoName);
		Analytics.trackAction(ACTION_VIDEO_END, contextData);
	}

	public static void trackActionTogglePower(String powerStatus) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : Power " + powerStatus);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, powerStatus);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionIndicatorLight(String lightStatus) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : IndicatorLight " + lightStatus);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, lightStatus);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionScheduleAdd() {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : ScheduleAdd");
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, VALUE_SCHEDULE_ADDED);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionTimerAdded(String time) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : TimerAdded " + time);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, VALUE_TIMER + " " + time);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionNotificationAirQuality(String airQuality) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : NotificationAQI " + airQuality);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, VALUE_NOTIFICATION_AIR_QUALITY	+ airQuality);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}
	
//	public static void trackActionNotificationReceived() {
//		ALog.i(ALog.TAGGING, "TrackAction : NotificationReceived " + airQuality);
//		Map<String, Object> contextData = new HashMap<String, Object>();
//		contextData.put("messageValue", VALUE_NOTIFICATION_AIR_QUALITY + airQuality);
//		Analytics.trackAction(ACTION_SET_OPTION, contextData);
//	}

	public static void trackActionFanSpeed(String speed) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : FanSpeed " + speed);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, VALUE_SPEED + " " + speed);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionChildLock(String childLockStatus) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : ChildLock " + childLockStatus);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, childLockStatus);
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionNotification(boolean notification) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : NotificationEnabled " + notification);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		if (notification) {
			contextData.put(KEY_OPTION_DETAILS, VALUE_NOTIFICATION_ON);
		} else {
			contextData.put(KEY_OPTION_DETAILS, VALUE_NOTIFICATION_OFF);
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionRemoteControl(boolean remote) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : RemoteEnabled " + remote);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		if (remote) {
			contextData.put(KEY_OPTION_DETAILS, VALUE_REMOTE_CONTROL_ON);
		} else {
			contextData.put(KEY_OPTION_DETAILS, VALUE_REMOTE_CONTROL_OFF);
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionAdvanceNetworkConfig(boolean config) {
		if(!trackMetrics) return;
		ALog.i(ALog.TAGGING, "TrackAction : AdvancedNetworkConfig " + config);
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		if (config) {
			contextData.put(KEY_OPTION_DETAILS, VALUE_ADVANCE_NETWORK_YES);
		} else {
			contextData.put(KEY_OPTION_DETAILS, VALUE_ADVANCE_NETWORK_NO);
		}
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}

	public static void trackActionAddSchedule() {
		if(!trackMetrics) return;
        ALog.i(ALog.TAGGING, "TrackAction : schedule added");
		Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_OPTION_DETAILS, "schedule added");
		Analytics.trackAction(ACTION_SET_OPTION, contextData);
	}
	
	private static Map<String, Object> addAnalyticsDataObject() {
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(KEY_APPNAME, VALUE_APPNAME);
		contextData.put(KEY_VERSION, PurAirApplication.getAppVersion());
		contextData.put(KEY_OS, ANDROID + Build.VERSION.RELEASE);
		contextData.put(KEY_LANGUAGE, getLanguage());
		contextData.put(KEY_CURRENCY, getCurrency());
        contextData.put(KEY_TIMESTAMP, getTimestamp(System.currentTimeMillis()));
		contextData.put(KEY_FIRMWARE_VERSION, getFirmwareVersion());
		contextData.put(KEY_MACHINE_ID, getDeviceEui64());
		contextData.put(KEY_PRODUCT_MODEL, AirPurifierManager.getInstance().getCurrentPurifier() != null ? VALUE_MODEL_AC4373 : "Not Found");
		contextData.put(KEY_APP_ID, CPPController.getInstance(PurAirApplication.getAppContext()).getAppCppId());
		return contextData;
	}

	private static String getFirmwareVersion() {
		AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier != null && currentPurifier.getFirmwarePort().getFirmwarePortInfo() != null) {
			String version = currentPurifier.getFirmwarePort().getFirmwarePortInfo().getVersion();
			return version;
		}
		return "Not found";
	}

	private static String getDeviceEui64() {
		AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier != null) {
			String eui64 = currentPurifier.getNetworkNode().getCppId();
			return eui64;
		}
		return "Not found";
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

    @SuppressLint("SimpleDateFormat")
	private static String getTimestamp(long timeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date(timeMillis));
        return date;
    }
}
