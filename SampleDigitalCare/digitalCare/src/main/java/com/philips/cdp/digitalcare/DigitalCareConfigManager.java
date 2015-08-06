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
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

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
    private String mPageName = null;
    private boolean mTaggingEnabled = false;

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
        if(mTaggingEnabled){
            if(mAppID==null || mAppID.equals("")){
                throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
            }
        }
        AnalyticsTracker.setTaggingInfo(mTaggingEnabled,mAppID);

        SupportHomeFragment supportFrag = new SupportHomeFragment();
        supportFrag.showFragment(context, parentContainerResId, supportFrag,
                actionbarUpdateListener, enterAnim, exitAnim);
    }

    public void invokeDigitalCareAsActivity(int startAnimation, int endAnimation, ActivityOrientation orientation) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        if(mTaggingEnabled){
            if(mAppID==null || mAppID.equals("")){
                throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
            }
        }
        AnalyticsTracker.setTaggingInfo(mTaggingEnabled,mAppID);

        Intent intent = new Intent(this.getContext(), DigitalCareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DigitalCareConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(DigitalCareConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(DigitalCareConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }

    public void setAppIdForTagging(String appId) throws RuntimeException {
        mAppID = appId;
    }

    public void enableTagging(boolean taggingEnabled){
        mTaggingEnabled = taggingEnabled;
    }

    public void setCurrentPageNameForTagging(String pageName){
        mPageName = pageName;
    }

    public String getVerticalPageNameForTagging(){
        return mPageName;
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

    public enum ActivityOrientation {


        SCREEN_ORIENTATION_UNSPECIFIED(-1), SCREEN_ORIENTATION_LANDSCAPE(0),
        SCREEN_ORIENTATION_PORTRAIT(1), SCREEN_ORIENTATION_USER(2), SCREEN_ORIENTATION_BEHIND(3),
        SCREEN_ORIENTATION_SENSOR(4),
        SCREEN_ORIENTATION_NOSENSOR(5),
        SCREEN_ORIENTATION_SENSOR_LANDSCAPE(6),
        SCREEN_ORIENTATION_SENSOR_PORTRAIT(7),
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE(8),
        SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
        SCREEN_ORIENTATION_FULL_SENSOR(10),
        SCREEN_ORIENTATION_USER_LANDSCAPE(11),
        SCREEN_ORIENTATION_USER_PORTRAIT(12),
        SCREEN_ORIENTATION_FULL_USER(13),
        SCREEN_ORIENTATION_LOCKED(14);
        private int value;

        ActivityOrientation(int value) {
            this.value = value;
        }

        private int getOrientationValue() {
            return value;
        }
    }

}
