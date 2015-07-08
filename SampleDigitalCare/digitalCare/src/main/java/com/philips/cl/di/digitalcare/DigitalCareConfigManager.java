package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cl.di.digitalcare.productdetails.ProductMenuListener;
import com.philips.cl.di.digitalcare.social.SocialProviderListener;

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
    public Locale mLocale = null;
    public Locale mLocaleMatchLocale = null;
    private String mTwitterConsumerKey = null;
    private String mTwitterConsumerSecret = null;
    private ConsumerProductInfo mConsumerProductInfo = null;
    private MainMenuListener mMainMenuListener = null;
    private ProductMenuListener mProductMenuListener = null;
    private SocialProviderListener mSocialProviderListener = null;


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
            initializeTaggingContext(mContext);
        }
    }

    public void invokeDigitalCareAsFragment(FragmentActivity context,
                                            int parentContainerResId,
                                            ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                            int exitAnim) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        SupportHomeFragment supportFrag = new SupportHomeFragment();
        //TODO: pending work on animation part.
        supportFrag.showFragment(context, parentContainerResId, supportFrag,
                actionbarUpdateListener, enterAnim, exitAnim);
    }

    public void invokeDigitalCareAsActivity(int startAnimation, int endAnimation) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        Intent intent = new Intent("android.intent.action.SUPPORT_DIGITAL");
        intent.putExtra("STARTANIMATIONID", startAnimation);
        intent.putExtra("ENDANIMATIONID", endAnimation);
        getContext().startActivity(intent);
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

    public void setLocale(String langCode, String countryCode) {

        if (langCode != null && countryCode != null) {

            mLocale = new Locale(langCode, countryCode);

            LocaleMatchHandler mLocaleMatchHandler = new LocaleMatchHandler(
                    mContext, langCode, countryCode);
            mLocaleMatchHandler.initializeLocaleMatchService();
        }
    }

    public Locale getLocale() {
        return mLocale;
    }

    public Locale getmLocaleMatchLocale() {
        return mLocaleMatchLocale;
    }

    public String getTwitterConsumerKey() {
        return mTwitterConsumerKey;
    }

    public String getTwitterConsumerSecret() {
        return mTwitterConsumerSecret;
    }

    public String getDigitalCareLibVersion(){
        return BuildConfig.VERSION_NAME;
    }

}
