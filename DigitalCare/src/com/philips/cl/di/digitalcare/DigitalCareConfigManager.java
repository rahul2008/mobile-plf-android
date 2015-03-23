package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * DigitalCareConfigManager is Application class for DigitalCare app. Here we
 * can maintain the instances at digital care app level. We need to pass the
 * parameters from hosting(integrating) apps and this class will initialize and
 * maintain for DigitalCareApp level.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 5 Dec 2014
 */
public class DigitalCareConfigManager {

	private Resources mResources = null;
	private static int[] mFeatureKeys = null;
	private static String mCountry = null;
	private static String mLanguage = null;
	private static int mAnimationStart = 0;
	private static int mAnimationStop = 0;
	private static String mTwitterConsumerKey = null;
	private static String mTwitterConsumerSecret = null;
	private static DigitalCareConfigManager mDigitalCareInstance = null;

	private static final String DEFAULT_LANGAUGE = "en";
	private static final String DEFAULT_COUNTRY = "GB";
	private static final int DEFAULT_ANIMATION_START = R.anim.slide_in_bottom;
	private static final int DEFAULT_ANIMATION_STOP = R.anim.slide_out_bottom;

	private static Context mContext = null;
	private static int mAppVersion = 0;

	/*
	 * Initialize everything(resources, variables etc) required for DigitalCare.
	 */
	public DigitalCareConfigManager(Context context) {
		if (mDigitalCareInstance == null) {
			mContext = context;
			initializeConfigManager();
		}
	}

	private DigitalCareConfigManager() {
		setConfigParametrs();
	}

	private void initializeConfigManager() {
		mDigitalCareInstance = new DigitalCareConfigManager();
	}

	/*
	 * Initializing values at first time.
	 */
	private void setConfigParametrs() {
		mResources = mContext.getResources();
		initializeFeaturesSupported();
		initializeAppVersion();
	}

	/*
	 * This method will parse, how many features are available at DigitalCare
	 * level.
	 */
	private void initializeFeaturesSupported() {
		String[] featuresAvailable = mResources
				.getStringArray(R.array.supported_features);
		mFeatureKeys = new int[featuresAvailable.length];

		for (int i = 0; i < featuresAvailable.length; i++) {
			try {
				mFeatureKeys[i] = Integer.parseInt(featuresAvailable[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeAppVersion() {
		try {
			PackageInfo packageInfo = mContext.getPackageManager()
					.getPackageInfo(mContext.getPackageName(), 0);
			DLog.i(DLog.APPLICATION, "Application version: "
					+ packageInfo.versionName + " (" + packageInfo.versionCode
					+ ")");
			mAppVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static int getAppVersion() {
		return mAppVersion;
	}

	/*
	 * This will give list of all buttons(features) on the Support Screen.
	 */
	public static int[] getFeatureListKeys() {
		return mFeatureKeys;
	}

	public static String getCountry() {
		return Utils.isEmpty(mCountry) ? DEFAULT_COUNTRY : mCountry;
	}

	public static void setCountry(String country) {
		mCountry = country;
	}

	public static String getLanguage() {
		return Utils.isEmpty(mLanguage) ? DEFAULT_LANGAUGE : mLanguage;
	}

	public static void setLanguage(String language) {
		mLanguage = language;
	}

	public static String getLocale() {
		return getLanguage() + "-" + getCountry();
	}

	/*
	 * If no start animation is set then take the default start animation.
	 */
	public static int getAnimationStart() {
		return mAnimationStart == 0 ? DEFAULT_ANIMATION_START : mAnimationStart;
	}

	public static void setAnimationStart(int animationStart) {
		mAnimationStart = animationStart;
	}

	/*
	 * If no start animation is set then take the default stop animation.
	 */
	public static int getAnimationStop() {
		return mAnimationStop == 0 ? DEFAULT_ANIMATION_STOP : mAnimationStop;
	}

	public static void setAnimationStop(int animationStop) {
		mAnimationStop = animationStop;
	}

	public static String getTwitterConsumerKey() {
		return mTwitterConsumerKey;
	}

	public static void setTwitterConsumerKey(String twitterConsumerKey) {
		mTwitterConsumerKey = twitterConsumerKey;
	}

	public static String getTwitterConsumerSecret() {
		return mTwitterConsumerSecret;
	}

	public static void setTwitterConsumerSecret(String twitterConsumerSecret) {
		mTwitterConsumerSecret = twitterConsumerSecret;
	}
}
