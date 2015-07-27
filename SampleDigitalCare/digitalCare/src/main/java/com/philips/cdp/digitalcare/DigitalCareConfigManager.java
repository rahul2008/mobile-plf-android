package com.philips.cdp.digitalcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigitalCareContants;

import java.util.Locale;

/**
 * DigitalCareConfigManager is Config class for DigitalCare app. Here we can
 * maintain the instances at digital care app level. We need to pass the
 * parameters from hosting(integrating) apps and this class will initialize and
 * maintain for DigitalCareApp level.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 */
public class DigitalCareConfigManager {

    private static DigitalCareConfigManager mDigitalCareInstance = null;
    private static Context mContext = null;
    private static LocaleMatchHandler mLocaleMatchHandler = null;
    private Locale mLocale = null;
    private Locale mLocaleMatchWithCountryFallBack = null;
    private Locale mLocaleMatchWithLanguageFallBack = null;
    private ConsumerProductInfo mConsumerProductInfo = null;
    private MainMenuListener mMainMenuListener = null;
    private ProductMenuListener mProductMenuListener = null;
    private SocialProviderListener mSocialProviderListener = null;
    private String mAppID = null;

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
    public static DigitalCareConfigManager getInstance() {
        if (mDigitalCareInstance == null) {
            mDigitalCareInstance = new DigitalCareConfigManager();
        }
        return mDigitalCareInstance;
    }

    private static void initializeTaggingContext(Context context) {
        AnalyticsTracker.isEnable(true);
        AnalyticsTracker.initContext(context);
    }

    public Context getContext() {
        return mContext;
    }

    public void initializeDigitalCareLibrary(Context applicationContext) {
        if (mContext == null) {
            DigitalCareConfigManager.mContext = applicationContext;
            mLocaleMatchHandler = new LocaleMatchHandler(mContext);
            initializeTaggingContext(mContext);
        }
    }

    public void invokeDigitalCareAsFragment(FragmentActivity context,
                                            int parentContainerResId,
                                            ActionbarUpdateListener actionbarUpdateListener, String enterAnim,
                                            String exitAnim) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        SupportHomeFragment supportFrag = new SupportHomeFragment();
        supportFrag.showFragment(context, parentContainerResId, supportFrag,
                actionbarUpdateListener, enterAnim, exitAnim);
    }

    public void invokeDigitalCareAsActivity(int startAnimation, int endAnimation) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        Intent intent = new Intent(this.getContext(), DigitalCareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DigitalCareContants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(DigitalCareContants.STOP_ANIMATION_ID, endAnimation);
        getContext().startActivity(intent);
    }

    public void setAppIdForTagging(String appId) throws RuntimeException {
        if (appId == null || appId.equals("")) {
            throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
        }
        mAppID = appId;
    }

    public String getAppIdForTagging() throws RuntimeException {
        if (mAppID == null || mAppID.equals("")) {
            throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
        }
        return mAppID;
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

    public void unregisterMainMenuListener(MainMenuListener mainMenuListener) {
        mMainMenuListener = null;
    }

    public MainMenuListener getMainMenuListener() {
        return mMainMenuListener;
    }

    public void registerProductMenuListener(
            ProductMenuListener productMenuListener) {
        mProductMenuListener = productMenuListener;
    }

    public void unregisterProductMenuListener(
            ProductMenuListener productMenuListener) {
        mProductMenuListener = null;
    }

    public ProductMenuListener getProductMenuListener() {
        return mProductMenuListener;
    }

    public void registerSocialProviderListener(
            SocialProviderListener socialProviderListener) {
        mSocialProviderListener = socialProviderListener;
    }

    public void unregisterSocialProviderListener(
            SocialProviderListener socialProviderListener) {
        mSocialProviderListener = null;
    }

    public SocialProviderListener getSocialProviderListener() {
        return mSocialProviderListener;
    }

    public void setLocale(String langCode, String countryCode) {

        mLocaleMatchWithCountryFallBack = null;
        mLocaleMatchWithLanguageFallBack=null;

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            mLocaleMatchHandler.initializeLocaleMatchService(langCode, countryCode);
        }
    }

    public Locale getLocale() {
        return mLocale;
    }

    public Locale getLocaleMatchResponseWithCountryFallBack() {
        return mLocaleMatchWithCountryFallBack;
    }

    public void setLocaleMatchResponseLocaleWithCountryFallBack(Locale localeMatchLocale) {
        mLocaleMatchWithCountryFallBack = localeMatchLocale;
    }


    public Locale getLocaleMatchResponseWithLanguageFallBack() {
        return mLocaleMatchWithLanguageFallBack;
    }

    public void setLocaleMatchResponseLocaleWithLanguageFallBack(Locale localeMatchLocale) {
        mLocaleMatchWithLanguageFallBack = localeMatchLocale;
    }

    public String getDigitalCareLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

}
