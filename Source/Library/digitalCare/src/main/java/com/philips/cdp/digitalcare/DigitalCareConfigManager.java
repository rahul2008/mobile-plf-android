/**
 * DigitalCareConfigManager is the Singleton class helps to manage,customize the features through
 * the supported API's.
 * <b> Note: </b>
 * <p> Few Methods may not relevant your requirement. As it playing the Horizontal Component
 * - API's are added by considering the commmon requirement  for the integrating applciations.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandlerObserver;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.launchertype.UiLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Locale;


public class DigitalCareConfigManager {

    private static final String TAG = DigitalCareConfigManager.class.getSimpleName();
    public static ProductModelSelectionType mProductModelSelectionType = null;
    public static String[] mCtnList = null;
    private static DigitalCareConfigManager mDigitalCareInstance = null;
    private static Context mContext = null;
    private static LocaleMatchHandler mLocaleMatchHandler = null;
    private static Locale mLocale = null;
    private static Locale mLocaleMatchWithCountryFallBack = null;
    //  private static Locale mLocaleMatchWithLanguageFallBack = null;
    private static LocaleMatchHandlerObserver mLocaleMatchHandlerObserver = null;
    private static UiLauncher mUiLauncher = null;
    private ConsumerProductInfo mConsumerProductInfo = null;
    private MainMenuListener mMainMenuListener = null;
    private ProductMenuListener mProductMenuListener = null;
    private SocialProviderListener mSocialProviderListener = null;
    private String mAppID = null;
    private String mAppName = null;
    private String mPageName = null;
    private boolean mTaggingEnabled = false;
    private ViewProductDetailsModel mProductDetailsModel = null;

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
     * <p>This is the DigitalCare initialization method. Please make sure to call this method
     * before invoking the DigitalComponent.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initializeDigitalCareLibrary(Context applicationContext) {
        if (mContext == null) {
            DigitalCareConfigManager.mContext = applicationContext;
            mLocaleMatchHandler = new LocaleMatchHandler(mContext);
            mLocaleMatchHandlerObserver = new LocaleMatchHandlerObserver();
            LocaleMatchHandler.initializePRXMap();
            initializeTaggingContext(mContext);
        }

        PILLocaleManager localeManager = new PILLocaleManager(mContext);
        String[] localeArray = new String[2];
        String locale = localeManager.getInputLocale();
        localeArray = locale.split("_");

        mLocale = new Locale(localeArray[0], localeArray[1]);
        if (mLocaleMatchWithCountryFallBack == null)
            mLocaleMatchWithCountryFallBack = mLocale;
           /* if (mLocaleMatchWithLanguageFallBack == null)
                mLocaleMatchWithLanguageFallBack = mLocale;*/
        mLocaleMatchHandler.initializeLocaleMatchService(localeArray[0], localeArray[1]);

    }

    public LocaleMatchHandlerObserver getObserver() {
        return mLocaleMatchHandlerObserver;
    }

    /**
     * <p> Invoking DigitalCareComponent feautures to your Fragment Container. Please use this
     * method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "digitalcare" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
     * <p> 2) Please make sure to set the Locale before calling this method.  </p>
     *
     * @param context                 Context of the FragmentActivity
     * @param parentContainerResId    Fragment container resource ID
     * @param actionbarUpdateListener ActionbarUpdateListener instance
     * @param enterAnim               Animation resource ID.
     * @param exitAnim                Animation resource ID.
     */
    private void invokeDigitalCareAsFragment(FragmentActivity context,
                                             int parentContainerResId,
                                             ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                             int exitAnim) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, before Support page is invoked");
        }

        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("") || (mAppName == null) || (mAppName == "") || (mPageName == null) || (mPageName == "")) {
                throw new RuntimeException("Please make sure to set the valid App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }
      /*  DigiCareLogger.i("testing", "DigitalCare Config -- Fragment Invoke");*/

        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        FragmentLauncher fragmentLauncher = new FragmentLauncher(context, parentContainerResId,
                actionbarUpdateListener);

        SupportHomeFragment supportFrag = new SupportHomeFragment();
        supportFrag.showFragment(/*context, parentContainerResId, */supportFrag,
                fragmentLauncher, enterAnim, exitAnim);
    }

    public void invokeDigitalCare(UiLauncher uiLauncher, ProductModelSelectionType productModelSelectionType) {
        mUiLauncher = uiLauncher;

        if (productModelSelectionType != null) {
            mProductModelSelectionType = productModelSelectionType;
            if (productModelSelectionType.getHardCodedProductList().length == 0)
                throw new IllegalStateException("Please make sure to set valid CTN before invoke");
        } else
            throw new IllegalArgumentException("Please make sure to set the valid ProductModelSelectionType object");

        if (uiLauncher instanceof ActivityLauncher) {

            DigiCareLogger.i(TAG, "Launching as Activity");
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            invokeDigitalCareAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
          /*  DigiCareLogger.i("testing", "DigitalCare Config -- Activity Invoke");*/

        } else {
            DigiCareLogger.i(TAG, "Launching through Fragment Manager instance");
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeDigitalCareAsFragment(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID(),
                    fragmentLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
          /*  DigiCareLogger.i("testing", "DigitalCare Config -- Fragment Invoke");*/
        }
    }

    public UiLauncher getUiLauncher() {
        return mUiLauncher;
    }

    /**
     * <p> Invoking DigitalCare Component from the Intent. </p>
     * <b> Note: </b> Please make sure to set the Locale before invoking this method.
     *
     * @param startAnimation Animation resource ID.
     * @param endAnimation   Animation Resource ID.
     * @param orientation
     */
    private void invokeDigitalCareAsActivity(int startAnimation, int endAnimation, com.philips.cdp.productselection.launchertype.ActivityLauncher.ActivityOrientation orientation) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context,  and locale before Support page is invoked");
        }
        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("") || (mAppName == null) || (mAppName == "") || (mPageName == null) || (mPageName == "")) {
                throw new RuntimeException("Please make sure to set the valid App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }
       /* DigiCareLogger.i("testing", "DigitalCare Config -- Activity Invoke");*/

        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        Intent intent = new Intent(this.getContext(), DigitalCareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DigitalCareConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(DigitalCareConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(DigitalCareConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }

    /**
     * @throws RuntimeException
     */
    public String getAppIdForTagging() throws RuntimeException {
        return mAppID;
    }

    /**
     * @throws RuntimeException
     */
    public String getAppNameForTagging() throws RuntimeException {
        return mAppName;
    }

    /**
     * This method provides to enable Debug Logs
     *
     * @param taggingEnabled True to enable & False to disable
     */
    public void setAppTaggingInputs(boolean taggingEnabled, String appId, String appName, String launchingPageName) {
        mTaggingEnabled = taggingEnabled;
        mPageName = launchingPageName;
        mAppName = appName;
        mAppID = appId;
    }

    public boolean isTaggingEnabled() {
        return mTaggingEnabled;
    }

    /**
     * It returns the previously set Page name for tagging.
     *
     * @return mPageScreenpageName
     */
    public String getPreviousPageNameForTagging() {
        return mPageName;
    }

    @SuppressWarnings("deprecation")
    public AppInfraInterface getAPPInfraInstance() {
        return AppInfraSingleton.getInstance();
    }

    /*public AppTaggingInterface getTaggingInterface() {
        AppTaggingInterface taggingInterface =
                getAPPInfraInstance().getTagging().createInstanceForComponent
                        ("com.philips.cdp.digitalcare", "6.0.0");
        taggingInterface.setPreviousPage("vertical:productSelection:home");
        return taggingInterface;
    }*/

    public LoggingInterface getLoggerInterface() {

        LoggingInterface loggingInterface = getAPPInfraInstance().getLogging().
                createInstanceForComponent("com.philips.cdp.digitalcare", "6.0.0");
        return loggingInterface;
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
        DigiCareLogger.d(TAG, "Country Fallback : " + localeMatchLocale.toString());
    }


    /*public Locale getLocaleMatchResponseWithLanguageFallBack() {
        return mLocaleMatchWithLanguageFallBack;
    }

    public void setLocaleMatchResponseLocaleWithLanguageFallBack(Locale localeMatchLocale) {
        mLocaleMatchWithLanguageFallBack = localeMatchLocale;
        DigiCareLogger.d(TAG, "Language Fallback : " + localeMatchLocale.toString());
    }*/

    public String getDigitalCareLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public ViewProductDetailsModel getViewProductDetailsData() {
        return mProductDetailsModel;
    }

    /*public boolean isBazaarVoiceRequired() {
        if (mContext != null) {
            return mContext.getResources().getBoolean(R.bool.productreview_required);
        }
        return false;
    }

    public boolean isProductionEnvironment() {
        if (mContext != null) {
            return mContext.getResources().getBoolean(R.bool.production_environment);
        }
        return false;
    }*/

    public void setViewProductDetailsData(ViewProductDetailsModel detailsObject) {
        mProductDetailsModel = detailsObject;
    }

    public ProductModelSelectionType getProductModelSelectionType() {
        return mProductModelSelectionType;
    }

    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the DigitalCare component from the Intent</p>
     * <p/>
     * <p> <b>Note : </b> The flags are similar to deafult android screen orientation flags</p>
     */
    /*public enum ActivityOrientation {


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
    }*/

}
