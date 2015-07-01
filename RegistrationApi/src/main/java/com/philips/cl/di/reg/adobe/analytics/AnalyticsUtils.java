
package com.philips.cl.di.reg.adobe.analytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.adobe.mobile.Analytics;
import com.philips.cl.di.reg.settings.RegistrationHelper;

public class AnalyticsUtils {

	public static void trackPage(String prevPage, String currPage) {
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.put(AnalyticsConstants.PREVIOUS_PAGE_NAME, prevPage);
		Analytics.trackState(currPage, contextData);
	}

	public static void trackAction(String state, String key, Object value) {
		Map<String, Object> contextData = addAnalyticsDataObject();
		if (null != key) {
			contextData.put(key, value);
		}
		Analytics.trackAction(state, contextData);
	}

	public static void trackMultipleActions(String state, Map<String, Object> map) {
		Map<String, Object> contextData = addAnalyticsDataObject();
		contextData.putAll(map);
		Analytics.trackAction(state, contextData);
	}

	private static Map<String, Object> addAnalyticsDataObject() {

		Map<String, Object> contextData = new HashMap<String, Object>();
		contextData.put(AnalyticsConstants.CP_KEY, AnalyticsConstants.CP_VALUE);
		contextData.put(AnalyticsConstants.APPNAME_KEY, AnalyticsConstants.APPNAME_VALUE);
		contextData.put(AnalyticsConstants.VERSION_KEY, RegistrationHelper.getInstance()
		        .getAppVersion());
		contextData.put(AnalyticsConstants.OS_KEY, AnalyticsConstants.OS_ANDROID);
		contextData.put(AnalyticsConstants.COUNTRY_KEY, RegistrationHelper.getInstance()
		        .getLocale().getCountry());
		contextData.put(AnalyticsConstants.LANGUAGE_KEY,  RegistrationHelper.getInstance()
		        .getLocale().getLanguage());
		contextData.put(AnalyticsConstants.CURRENCY_KEY, getCurrency());
		contextData.put(AnalyticsConstants.APPSID_KEY, Analytics.getTrackingIdentifier());
		contextData.put(AnalyticsConstants.TIMESTAMP_KEY, getTimestamp());

		return contextData;
	}

	private static String getCurrency() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		if (currencyCode == null)
			currencyCode = AnalyticsConstants.DEFAULT_CURRENCY;
		return currencyCode;
	}

	private static String getTimestamp() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}
}
