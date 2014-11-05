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

	private static String CP_KEY = "sector";
	private static String APPNAME_KEY = "app.name";
	private static String VERSION_KEY = "app.version";
	private static String OS_KEY = "app.os";
	private static String COUNTRY_KEY = "locale.country";
	private static String LANGUAGE_KEY = "locale.language";
	private static String CURRENCY_KEY = "locale.currency";

	private static String CP_VALUE = "CP";
	private static String APPNAME_VALUE = "PurAir";
	
	public static void initContext(Context context) {
		Config.setContext(context);
	}
	
	public static void startCollectLifecycleData(Activity activity) {
		Config.collectLifecycleData(activity);
	}
	
	public static void stopCollectLifecycleData() {
		Config.pauseCollectingLifecycleData();
	}
	
	public static void trackPage(String pageName) {
		Analytics.trackState(pageName, addAnalyticsDataObject());
	}
	
	private  static Map<String, Object> addAnalyticsDataObject(){
		System.out.println("ADBMobile.addAnalyticsDataObject()");
		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(CP_KEY, CP_VALUE);
		contextData.put(APPNAME_KEY, APPNAME_VALUE);
		contextData.put(VERSION_KEY, PurAirApplication.getAppVersion());
		contextData.put(OS_KEY, "Android " + Build.VERSION.RELEASE);
		contextData.put(COUNTRY_KEY, getCountry());
		contextData.put(LANGUAGE_KEY, getLanguage());
		contextData.put(CURRENCY_KEY, getCurrency());
		return contextData;
	}
	
	private static String getCountry() {
		String country = PurAirApplication.getAppContext().getResources().getConfiguration().locale.getCountry().toLowerCase(Locale.getDefault());
		if(country==null)
			country = DEFAULT_COUNTRY;
		return country;
	}
	
	private static String getLanguage() {
		String language = PurAirApplication.getAppContext().getResources().getConfiguration().locale.getLanguage();
		if(language==null)
			language = DEFAULT_LANGUAGE;
		return language;
	}
	
	private static String getCurrency() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		if(currencyCode==null)
			currencyCode= DEFAULT_CURRENCY;
		return currencyCode;
	}

}
