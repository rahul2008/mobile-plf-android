package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.res.Resources;

/*
 *	DigitalCareConfigManager is Application class for DigitalCare app. 
 *  Here we can maintain the instances at digital care app level. 
 *  We need to pass the parameters from hosting(integrating) apps and 
 *  this class will initialize and maintain for DigitalCareApp level.
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
	private static String mAnimationStart = null;
	private static String mAnimationStop = null;
	private static String mTwitterConsumerKey = null;
	private static String mTwitterConsumerSecret = null;
	private static DigitalCareConfigManager mDigitalCareInstance = null;

	/*
	 * Initialize everything required for DigitalCare.
	 */
	private DigitalCareConfigManager(Context context) {
		setConfigParametrs(context);
	}

	private void setConfigParametrs(Context context) {
		mResources = context.getResources();
		getFeaturesAvailable();
	}

	/*
	 * Singleton.
	 */
	public static DigitalCareConfigManager getInstance(Context context) {
		if (mDigitalCareInstance == null) {
			mDigitalCareInstance = new DigitalCareConfigManager(context);
		}
		return mDigitalCareInstance;
	}

	/*
	 * This method will parse, how many features are available at DigitalCare
	 * level.
	 */
	private void getFeaturesAvailable() {
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

	/*
	 * This will give list of all buttons(features) on the Support Screen.
	 */
	public int[] getFeatureListKeys() {
		return mFeatureKeys;
	}

	public static String getCountry() {
		return mCountry;
	}

	public static void setCountry(String country) {
		mCountry = country;
	}

	public static String getLanguage() {
		return mLanguage;
	}

	public static void setLanguage(String language) {
		mLanguage = language;
	}

	public static String getLocale() {
		return getLanguage() + "-" + getCountry();
	}

	public static String getAnimationStart() {
		return mAnimationStart;
	}

	public static void setAnimationStart(String animationStart) {
		mAnimationStart = animationStart;
	}

	public static String getAnimationStop() {
		return mAnimationStop;
	}

	public static void setAnimationStop(String animationStop) {
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
