package com.philips.cl.di.localematch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.philips.cl.di.localematch.enums.Catalog;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.localematch.enums.Sector;
import android.util.Log;

/**
 * @author Deepthi Shivakumar
 * 
 */
public class PILLocaleManager {

	private LocaleMatchListener mLocaleMatchListener;

	private static final String PREF_INPUT_LOCALE = "input_locale";

	private static final String LOCALEMATCH_PREFERENCE_NAME = "LOCALEMATCH_PREFERENCE";

	private static final String MATCH_BY_LANGUAGE = "byLanguage";

	private static final String MATCH_BY_COUNTRY = "byCountry";

	private static final String LOCALES = "locales";

	private static final String JSONARRAY_MATCH = "match";

	private static final String JSONARRAY_PLATFORM = "platform";

	private static final String JSON_SECTOR = "sector";

	private static final String JSON_CATALOG = "catalog";

	private static final String JSONOBJ_DATA = "data";

	private static final String JSON_SUCCESS = "success";

	private static final String JSON_AVAILABLE = "available";

	private static final String JSON_ID = "id";
	
	private static final String LOG_TAG ="PILLocaleManager";

	public void init(Context context, LocaleMatchListener listener) {
		Log.i(LOG_TAG, "LocaleMatch init()");
		LocaleMatchNotifier notifier = LocaleMatchNotifier.getIntance();
		notifier.registerForLocaleMatchChange(listener);
		mLocaleMatchListener = listener;
	}

	public void refresh(Context context, String languageCode, String countryCode) {
		Log.i(LOG_TAG, "LocaleMatch refresh(), lang = "
				+ languageCode + "country code = " + countryCode);
		String inputLocale = languageCode + "_" + countryCode;
		boolean refreshNeeded = IsForceRefreshNeeded(context, inputLocale);
		Log.i(LOG_TAG, "refresh(), refreshNeeded = "
				+ refreshNeeded);
		if (refreshNeeded) {
			setInputLocale(context, languageCode, countryCode);
			forceRefresh(context, languageCode, countryCode);
		} else {
			mLocaleMatchListener.onLocaleMatchRefreshed(inputLocale);
		}
	}

	public static void setInputLocale(Context context, String languageCode,
			String countryCode) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				LOCALEMATCH_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String inputLocale = languageCode + "_" + countryCode;
		Editor editor = sharedPreferences.edit();
		editor.putString(PREF_INPUT_LOCALE, inputLocale);
		boolean committed = editor.commit();
		Log.i(LOG_TAG, "committed = " + committed);
	}

	public static String getInputLocale(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				LOCALEMATCH_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (sharedPreferences != null) {
			Log.i(LOG_TAG, "getInputLocale not null");
			return sharedPreferences.getString(PREF_INPUT_LOCALE, null);
		}
		Log.i(LOG_TAG, "getInputLocale returning null");
		return null;
	}

	

	private boolean IsForceRefreshNeeded(Context context, String inputLocale) {
		String inputLocalePref = getInputLocale(context);
		boolean refreshNeeded = false;
		Log.i(LOG_TAG,
				"LocaleMatch refresh(), inputLocalePref = " + inputLocalePref);

		if (inputLocalePref == null) {
			Log.i(LOG_TAG, "null");
			refreshNeeded = true;
		} else if ((inputLocale != null)
				&& (inputLocale.equalsIgnoreCase(inputLocalePref))) {
			Log.i(LOG_TAG, "INPUT SAME");
			boolean fileExists = LocaleMatchFileHelper.verifyJsonExists(inputLocale);
			if (fileExists) {
				refreshNeeded = false;
			} else {
				boolean isProcessing = LocaleMatchThreadManager
						.isProcessingRequest(inputLocale);
				if (!isProcessing) {
					refreshNeeded = true;
				}
			}
		} else if ((inputLocale != null)
				&& (!inputLocale.equalsIgnoreCase(inputLocalePref))) {
			Log.i(LOG_TAG, "INPUT LOCALE DIFFERS");
			refreshNeeded = true;
		}
		return refreshNeeded;
	}

	private void forceRefresh(Context context, String languageCode,
			String countryCode) {
		Log.i(LOG_TAG, "LocaleMatch forcerefresh()");
		LocaleMatchThreadManager threadManager = new LocaleMatchThreadManager();
		threadManager.processRequest(languageCode, countryCode);
	}

	public PILLocale currentLocaleWithCountryFallbackForPlatform(String locale,
			Platform platform, Sector sector, Catalog catalog) {
		return parseLocaleMatchJson(locale, platform, sector, catalog,
				MATCH_BY_COUNTRY);
	}

	public PILLocale currentLocaleWithLanguageFallbackForPlatform(
			String locale, Platform platform, Sector sector, Catalog catalog) {
		return parseLocaleMatchJson(locale, platform, sector, catalog,
				MATCH_BY_LANGUAGE);
	}

	private PILLocale parseLocaleMatchJson(String locale, Platform platform,
			Sector sector, Catalog catalog, String matchContraint) {
		String responseStr = LocaleMatchFileHelper
				.getJsonStringFromFile(locale);
		JSONArray localeArr = getLocaleParsingJson(responseStr, platform,
				sector, catalog, matchContraint);		
		return getPIlLocale(getResultedLocaleFromLocaleArray(localeArr, locale));
	}

	private JSONArray getLocaleParsingJson(String jsonStr, Platform platform,
			Sector sector, Catalog catalog, String matchContraint) {
		if ((jsonStr == null) || (platform.toString() == null)
				|| (sector.toString() == null) || (catalog.toString() == null)
				|| (matchContraint.toString() == null)) {
			Log.i(LOG_TAG,"getLocaleParsingJson(), PARAMETERS NULL");
			return null;
		}
		try {
			JSONObject jsonobj = new JSONObject(jsonStr);
			String success = jsonobj.getString(JSON_SUCCESS);
			
			if ((success.equalsIgnoreCase("true")) && jsonobj.has(JSONOBJ_DATA)) {
				JSONObject dataObj = jsonobj.getJSONObject(JSONOBJ_DATA);
				JSONArray platformArray = dataObj.getJSONArray(JSONARRAY_PLATFORM);
				for (int platformId = 0; platformId < platformArray.length(); platformId++) {
					JSONObject platformObj = platformArray
							.getJSONObject(platformId);
					if (platform.toString().equals(platformObj.getString(JSON_ID))) {
						JSONArray matchArray = platformObj.getJSONArray(JSONARRAY_MATCH);
						for (int sectorIndex = 0; sectorIndex < matchArray
								.length(); sectorIndex++) {
							JSONObject sectorObj;
							sectorObj = matchArray.getJSONObject(sectorIndex);
							if ((sector.toString().equalsIgnoreCase(sectorObj.getString(JSON_SECTOR)))
									&& (catalog.toString().equalsIgnoreCase(sectorObj.getString(JSON_CATALOG)))) {
								if (sectorObj.has(matchContraint)) {
									JSONObject localeObj = sectorObj.getJSONObject(matchContraint);
									if (localeObj.getString(JSON_AVAILABLE).equalsIgnoreCase("true")) {
										JSONArray jsonLocaleArr = localeObj.getJSONArray(LOCALES);
										return jsonLocaleArr;
									}
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i(LOG_TAG,"getLocaleParsingJson, JSONException");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(LOG_TAG,"getLocaleParsingJson, GENERIC Exception");
		}
		return null;
	}

	private String getResultedLocaleFromLocaleArray(JSONArray localeArr, String inputLocale) {
		String resultedLocale = null;
		Log.i(LOG_TAG, "getPIlLocale, inputLOcale = "
				+ inputLocale);
		if (localeArr == null || inputLocale == null) {
			Log.i(LOG_TAG,"localeArr = null or inputLocale == null");
			return null;
		}
		int localeArrLen = localeArr.length();
		String locale;
		try {
			if (localeArrLen == 1) {
				locale = localeArr.getString(0);
				if (locale != null) {
					resultedLocale = locale;
				}
			} else if (localeArrLen >1 ){
				String inputLocaleArr[] = inputLocale.split("_");
				String ipLangCode = inputLocaleArr[0];
				boolean matchFound = false;
				for (int localeIndex = 0; localeIndex < localeArrLen; localeIndex++) {
					locale = localeArr.getString(localeIndex);
					if (inputLocaleArr != null && inputLocaleArr.length > 0) {
						if (locale.contains(ipLangCode)) {
							resultedLocale = locale;
							matchFound = true;
							break;
						}
					}
				}
				if (!matchFound) {
					resultedLocale = localeArr.getString(0);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultedLocale;
	}
	
	private PILLocale getPIlLocale(String resultLocale){
		String resultLocaleArr[] = null;
		PILLocale PILLocaleInstance = new PILLocale();
		
		if(resultLocale!=null){
			resultLocaleArr = resultLocale.split("_"); 
		}
		if (resultLocaleArr != null && resultLocaleArr.length > 0) {
			PILLocaleInstance.setLanguageCode(resultLocaleArr[0]);
			PILLocaleInstance.setCountryCode(resultLocaleArr[1]);
			PILLocaleInstance.setLocaleCode(resultLocale);
			Log.i(LOG_TAG,
					"getPIlLocale(), Resulted Locale = " + resultLocale);
			return PILLocaleInstance;
		}
		return null;
	}

	public static String getLacaleMatchVersion(){
		return BuildConfig.VERSION_NAME;
	}

}
