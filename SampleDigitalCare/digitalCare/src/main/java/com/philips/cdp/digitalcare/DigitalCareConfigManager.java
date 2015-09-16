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
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandlerObserver;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.rateandreview.parser.ProductPageParser;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

import java.util.Locale;

/**
 * DigitalCareConfigManager is the Singleton classe helps to manage,customize the features through the supported API's
 * <p>
 * <b> Note: </b>
 * <p>      Few Methods may not relevant your requirement. As it playing the Horizontal Component - API's are added by considering the
 * commmon requirement  for the integrating applciations.
 * </p></p>
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 */
public class DigitalCareConfigManager {

    private static DigitalCareConfigManager mDigitalCareInstance = null;
    private static Context mContext = null;
    private static LocaleMatchHandler mLocaleMatchHandler = null;
    private static Locale mLocale = null;
    private static Locale mLocaleMatchWithCountryFallBack = null;
    private static Locale mLocaleMatchWithLanguageFallBack = null;
    private ConsumerProductInfo mConsumerProductInfo = null;
    private MainMenuListener mMainMenuListener = null;
    private ProductMenuListener mProductMenuListener = null;
    private SocialProviderListener mSocialProviderListener = null;
    private String mAppID = null;
    private String mPageName = null;
    private boolean mTaggingEnabled = false;
    private static LocaleMatchHandlerObserver mLocaleMatchHandlerObserver=null;

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

    /**
     * Returs the Context used in the DigitalCare Component
     *
     * @return Returns the Context using by the Component.
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * <p>This is the DigitalCare initialization method. Please make sure to call this method before invoking the DigitalComponent.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initializeDigitalCareLibrary(Context applicationContext) {
        if (mContext == null) {
            DigitalCareConfigManager.mContext = applicationContext;
            mLocaleMatchHandler = new LocaleMatchHandler(mContext);
            mLocaleMatchHandlerObserver=new LocaleMatchHandlerObserver();
            LocaleMatchHandler.initializePRXMap();
            initializeTaggingContext(mContext);
        }
    }

    public LocaleMatchHandlerObserver getObserver(){
        return mLocaleMatchHandlerObserver;
    }

    /**
     * <p> Invoking DigitalCareComponent feautures to your Fragment Container. Please use this method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "digitalcare" to identify the MainScreen Fragment as a Fragment ID. </p>
     * <p> 2) Please make sure to set the Locale before calling this method.  </p>
     *
     * @param context Context of the FragmentActivity
     * @param parentContainerResId Fragment container resource ID
     * @param actionbarUpdateListener ActionbarUpdateListener instance
     * @param enterAnim Animation resource ID.
     * @param exitAnim Animation resource ID.
     */
    public void invokeDigitalCareAsFragment(FragmentActivity context,
                                            int parentContainerResId,
                                            ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                            int exitAnim) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("")) {
                throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
            }
        }
        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        SupportHomeFragment supportFrag = new SupportHomeFragment();
        supportFrag.showFragment(context, parentContainerResId, supportFrag,
                actionbarUpdateListener, enterAnim, exitAnim);
    }


    /**
     * <p> Invoking DigitalCare Component from the Intent. </p>
     * <b> Note: </b> Please make sure to set the Locale before invoking this method.
     *
     * @param startAnimation  Animation resource ID.
     * @param endAnimation Animation Resource ID.
     * @param orientation {@link com.philips.cdp.digitalcare.DigitalCareConfigManager.ActivityOrientation} flag.
     */
    public void invokeDigitalCareAsActivity(int startAnimation, int endAnimation, ActivityOrientation orientation) {
        if (mContext == null || mConsumerProductInfo == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }
        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("")) {
                throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
            }
        }
        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        Intent intent = new Intent(this.getContext(), DigitalCareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DigitalCareConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(DigitalCareConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(DigitalCareConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }

    /**
     * This method allows to set the TaggingID.
     *
     * @param appId APPID for the Tagging purpose
     * @throws RuntimeException
     */
    public void setAppIdForTagging(String appId) throws RuntimeException {
        mAppID = appId;
    }

    /**
     * This method provides to enable Debug Logs
     *
     * @param taggingEnabled True to enable & False to disable
     */
    public void enableTagging(boolean taggingEnabled) {
        mTaggingEnabled = taggingEnabled;
    }


    /**
     * Set the Page name for the Tagging.
     *
     * @param pageName PageName in String format
     */
    public void setCurrentPageNameForTagging(String pageName) {
        mPageName = pageName;
    }


    /**
     * It returns the previously set Page name for tagging.
     *
     * @return
     */
    public String getVerticalPageNameForTagging() {
        return mPageName;
    }


    /**
     * Returns {@link ConsumerProductInfo} object.
     *
     * @return Returns the instance of {@link ConsumerProductInfo} object using in the DigitalCare module.
     */
    public ConsumerProductInfo getConsumerProductInfo() {
        return mConsumerProductInfo;
    }


    /**
     * Set the {@link ConsumerProductInfo} object with valid credentials.
     *
     * @param info {@link ConsumerProductInfo} object.
     */
    public void setConsumerProductInfo(ConsumerProductInfo info) {
        mConsumerProductInfo = info;
    }


    /**
     * set the {@link MainMenuListener} object.
     *
     * @param mainMenuListener MainMenuListener interface object.
     */
    public void registerMainMenuListener(MainMenuListener mainMenuListener) {
        mMainMenuListener = mainMenuListener;
    }

    /**
     * Removing the {@link MainMenuListener} object.
     *
     * @param mainMenuListener MainMenuListener interface object.
     */
    public void unregisterMainMenuListener(MainMenuListener mainMenuListener) {
        mMainMenuListener = null;
    }

    /**
     * Returns {@link MainMenuListener} object.
     *
     * @return Returns the MainMenuListener object using across the DigitalCare component.
     */
    public MainMenuListener getMainMenuListener() {
        return mMainMenuListener;
    }


    /**
     * set the {@link ProductMenuListener} object.
     *
     * @param productMenuListener ProductMenuListener object.
     */
    public void registerProductMenuListener(
            ProductMenuListener productMenuListener) {
        mProductMenuListener = productMenuListener;
    }

    /**
     * Unregister {@link ProductMenuListener} Object.
     *
     * @param productMenuListener ProductMenuListener object.
     */
    public void unregisterProductMenuListener(
            ProductMenuListener productMenuListener) {
        mProductMenuListener = null;
    }

    /**
     * Returns the {@link ProductMenuListener} object.
     *
     * @return {@link ProductMenuListener}
     */
    public ProductMenuListener getProductMenuListener() {
        return mProductMenuListener;
    }


    /**
     * set the {@link SocialProviderListener} object.
     *
     * @param socialProviderListener (@link socialProviderListener)
     */
    public void registerSocialProviderListener(
            SocialProviderListener socialProviderListener) {
        mSocialProviderListener = socialProviderListener;
    }

    /**
     * Remove the {@link SocialProviderListener} object.
     *
     * @param socialProviderListener {@link SocialProviderListener}
     */
    public void unregisterSocialProviderListener(
            SocialProviderListener socialProviderListener) {
        mSocialProviderListener = null;
    }

    /**
     * Returns the {@link SocialProviderListener} object.
     *
     * @return {@link SocialProviderListener} object using across the DigitalCare component.
     */
    public SocialProviderListener getSocialProviderListener() {
        return mSocialProviderListener;
    }


    /**
     * <p> Set the Locale to the DigitalCare Component </p>
     * <p> This Locale is considered for the Localized language as well as Locale specific Philips Server data comminication </p>
     * <p></p>
     * <b>Note: </b>
     * <p>  - This is very important method, So please make sure to call this before invoking the DigitalCare Components</p>
     *
     * @param langCode LanguageCode
     * @param countryCode CountryCode
     */
    public void setLocale(String langCode, String countryCode) {

//        mLocaleMatchWithCountryFallBack = null;
//        mLocaleMatchWithLanguageFallBack = null;

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            mLocaleMatchHandler.initializeLocaleMatchService(langCode, countryCode);
        }
    }

    /**
     * Returns Locale used in the DigitalCare Component
     *
     * @return Retuns the {@link Locale} object using by DigitalCare component.
     */
    public Locale getLocale() {
        return mLocale;
    }

    public Locale getLocaleMatchResponseWithCountryFallBack() {
        return mLocaleMatchWithCountryFallBack;
    }

    public void setLocaleMatchResponseLocaleWithCountryFallBack(Locale localeMatchLocale) {
        mLocaleMatchWithCountryFallBack = localeMatchLocale;
       new ProductPageParser().execute();
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

    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the DigitalCare component from the Intent</p>
     *
     * <p> <b>Note : </b> The flags are similar to deafult android screen orientation flags</p>
     */
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
