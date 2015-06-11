package com.philips.cl.di.digitalcare;

import java.util.Locale;

import android.content.Context;

import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.productdetails.ProductMenuListener;
import com.philips.cl.di.digitalcare.social.SocialProviderListener;

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

	private static Locale mLocale = null;
	private static String mTwitterConsumerKey = null;
	private static String mTwitterConsumerSecret = null;
	private static DigitalCareConfigManager mDigitalCareInstance = null;
	private ConsumerProductInfo mConsumerProductInfo = null;

	private MainMenuListener mMainMenuListener = null;
	private ProductMenuListener mProductMenuListener = null;
	private SocialProviderListener mSocialProviderListener = null;

	// Twitter APP SDK API KEYS
	private static final String DEFAULT_TWITTER_CONSUMER_KEY = "qgktZw1ffdoreBjbiYfvnIPJe";
	private static final String DEFAULT_TWITTER_SECRET_KEY = "UUItcyGgL9v2j2vBBh9p5rHIuemsOlHdkMiuIMJ7VphlG38JK3";

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
			ProductMenuListener productMenuListener) {
		mProductMenuListener = productMenuListener;
	}

	public ProductMenuListener getProductMenuListener() {
		return mProductMenuListener;
	}

	public void registerSocialProviderListener(
			SocialProviderListener socialProviderListener) {
		mSocialProviderListener = socialProviderListener;
	}

	public SocialProviderListener getSocialProviderListener() {
		return mSocialProviderListener;
	}

	public static void setLocale(Locale locale) {
		mLocale = locale;
	}

	public static Locale getLocale() {
		return mLocale;
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
