package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.productdetails.ProductMenuButtonClickListner;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * DigitalCareConfigManager is Config class for DigitalCare app. Here we can
 * maintain the instances at digital care app level. We need to pass the
 * parameters from hosting(integrating) apps and this class will initialize and
 * maintain for DigitalCareApp level.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 5 Dec 2014
 */
public class DigitalCareConfigManager {

	private static String mCountry = null;
	private static String mLanguage = null;
	private static int mAnimationStart = 0;
	private static int mAnimationStop = 0;
	private static String mTwitterConsumerKey = null;
	private static String mTwitterConsumerSecret = null;
	private static DigitalCareConfigManager mDigitalCareInstance = null;
	private ConsumerProductInfo mConsumerProductInfo = null;

	private static final String DEFAULT_LANGAUGE = "en";
	private static final String DEFAULT_COUNTRY = "GB";
	private static final int DEFAULT_ANIMATION_START = R.anim.slide_in_bottom;
	private static final int DEFAULT_ANIMATION_STOP = R.anim.slide_out_bottom;

	private MainMenuListener mMainMenuListener = null;
	private ProductMenuButtonClickListner mProductMenuListener = null;
	private SocialProviderListener mSocialProviderListener = null;

	// Twitter APP SDK API KEYS
	private static final String DEFAULT_TWITTER_CONSUMER_KEY = "qgktZw1ffdoreBjbiYfvnIPJe";
	private static final String DEFAULT_TWITTER_SECRET_KEY = "UUItcyGgL9v2j2vBBh9p5rHIuemsOlHdkMiuIMJ7VphlG38JK3";

	// Launching Screen
	private static String mLaunchingScreen = DigitalCareContants.OPTION_SUPPORT_SCREEN;

	private static Context mContext = null;

	/*
	 * Initialize everything(resources, variables etc) required for DigitalCare.
	 * Hosting app, which will integrate this DigitalCare, has to pass app
	 * context.
	 */
	private DigitalCareConfigManager() {
	}

	/*
	 * Singleton pattern.
	 */
	public static DigitalCareConfigManager getInstance(Context context) {
		if (mDigitalCareInstance == null) {
			mContext = context;
			mDigitalCareInstance = new DigitalCareConfigManager();
			initializeTaggin(context);
		}
		return mDigitalCareInstance;
	}

	public static Context getContext() {
		return mContext;
	}

	public ConsumerProductInfo getConsumerProductInfo() {
		return mConsumerProductInfo;
	}

	public void setConsumerProductInfo(ConsumerProductInfo info) {
		mConsumerProductInfo = info;
	}

	public void registerMainMenuListener(MainMenuListener mainMenuListener) {
		mMainMenuListener = mainMenuListener;
	}

	public MainMenuListener getMainMenuListener() {
		return mMainMenuListener;
	}

	public void registerProductMenuListener(
			ProductMenuButtonClickListner productMenuListener) {
		mProductMenuListener = productMenuListener;
	}

	public ProductMenuButtonClickListner getProductMenuListener() {
		return mProductMenuListener;
	}

	public void registerSocialProviderListener(
			SocialProviderListener socialProviderListener) {
		mSocialProviderListener = socialProviderListener;
	}

	public SocialProviderListener getSocialProviderListener() {
		return mSocialProviderListener;
	}

	/*
	 * Setting user defined screen. User can access any screen which they want.
	 */
	public static void setLaunchingScreen(Context context, String launchScreen) {
		mLaunchingScreen = launchScreen;
		launchComponent(context);
	}

	private static void launchComponent(Context context) {
		Intent intent = new Intent(context, DigitalCareActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/*
	 * Getting User defined screen.
	 */
	public static String getLaunchingScreen() {
		return mLaunchingScreen;
	}

	public static int getAppVersion() {
		int appVersion = 0;
		try {
			PackageInfo packageInfo = mContext.getPackageManager()
					.getPackageInfo(mContext.getPackageName(), 0);
			DLog.i(DLog.APPLICATION, "Application version: "
					+ packageInfo.versionName + " (" + packageInfo.versionCode
					+ ")");
			appVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
		return appVersion;
	}

	/*
	 * App's package name.
	 */
	public static String getAppPackageName() {
		// TODO: We are putting hardcoded air fryer store link. Once integration
		// will happen then we
		// can remove this temp code.
		// return mContext.getApplicationContext().getPackageName();

		return "com.philips.cl.di.kitchenappliances.airfryer";
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
		return getLanguage() + "_" + getCountry();
	}

	/*
	 * If no start animation is not set from Hosting(Parent) app then we can use
	 * the default.
	 */
	public static int getAnimationStart() {
		return mAnimationStart == 0 ? DEFAULT_ANIMATION_START : mAnimationStart;
	}

	public static void setAnimationStart(int animationStart) {
		mAnimationStart = animationStart;
	}

	/*
	 * If no stop animation is not set from Hosting(Parent) app then we can use
	 * the default.
	 */
	public static int getAnimationStop() {
		return mAnimationStop == 0 ? DEFAULT_ANIMATION_STOP : mAnimationStop;
	}

	public static void setAnimationStop(int animationStop) {
		mAnimationStop = animationStop;
	}

	// If the consumer key is not set from Hosting(Parent) app then we can use
	// the dummy/default.
	public static String getTwitterConsumerKey() {
		return mTwitterConsumerKey == null ? DEFAULT_TWITTER_CONSUMER_KEY
				: mTwitterConsumerKey;
	}

	public static void setTwitterConsumerKey(String twitterConsumerKey) {
		mTwitterConsumerKey = twitterConsumerKey;
	}

	// If the consumer key is not set from Hosting(Parent) app then we can use
	// the dummy/default.
	public static String getTwitterConsumerSecret() {
		return mTwitterConsumerSecret == null ? DEFAULT_TWITTER_SECRET_KEY
				: mTwitterConsumerSecret;
	}

	public static void setTwitterConsumerSecret(String twitterConsumerSecret) {
		mTwitterConsumerSecret = twitterConsumerSecret;
	}

	private static void initializeTaggin(Context context) {
		AnalyticsTracker.isEnable(true);
		AnalyticsTracker.initContext(context);
	}
}
