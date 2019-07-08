/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
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
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.Locale;

public class DigitalCareConfigManager {

    private static final String TAG = DigitalCareConfigManager.class.getSimpleName();
    public static ProductModelSelectionType mProductModelSelectionType = null;
    public static String[] mCtnList = null;
    private static DigitalCareConfigManager mDigitalCareInstance = null;
    private Context mContext = null;
    private static Locale mLocale = null;
    private UiLauncher mUiLauncher = null;
    AppInfraInterface mAppInfraInterface;
    private ConsumerProductInfo mConsumerProductInfo = null;
    private CcListener mCcListener = null;
    private String mAppID = null;
    private String mAppName = null;
    private String mPageName = null;
    private boolean mTaggingEnabled = false;
    private ViewProductDetailsModel mProductDetailsModel = null;
    private String liveChatUrl = null;
    private String subCategoryUrl = null;
    private String cdlsUrl = null;
    private String atosUrl = null;
    private String emailUrl = null;
    private String productReviewUrl = null;

    private String fbUrl = null;
    private String twitterUrl = null;

    private String sdLiveChatUrl = null;

    private String country = null;
    private static int DLS_THEME;
    private static ThemeConfiguration themeConfiguration;

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

    /**
     * Returs the Context used in the DigitalCare Component
     *
     * @return Returns the Context using by the Component.
     */
    public Context getContext() {
        return mContext;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * <p>This is the DigitalCare initialization method. Please make sure to call this method
     * before invoking the DigitalComponent.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initializeDigitalCareLibrary(Context applicationContext, AppInfraInterface
            appInfraInterface) {
        mContext = applicationContext;
        mAppInfraInterface = appInfraInterface;

        ProductModelSelectionHelper.getInstance().initialize(mContext, mAppInfraInterface);
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
    protected void invokeDigitalCareAsFragment(FragmentActivity context,
                                               int parentContainerResId,
                                               ActionBarListener actionbarUpdateListener, int enterAnim,
                                               int exitAnim) {
        if (mContext == null) {
            throw new RuntimeException("Please initialise context, before Support page is invoked");
        }

        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.isEmpty() || (mAppName == null) || (mAppName.isEmpty()) || (mPageName == null) || (mPageName.isEmpty())) {
                throw new RuntimeException("Please make sure to set the valid App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }

        FragmentLauncher fragmentLauncher = new FragmentLauncher(context, parentContainerResId,
                actionbarUpdateListener);

        SupportHomeFragment supportFrag = new SupportHomeFragment();
        supportFrag.showFragment(/*context, parentContainerResId, */supportFrag,
                fragmentLauncher, enterAnim, exitAnim);
    }

    protected void invokeDigitalCare(UiLauncher uiLauncher, ProductModelSelectionType productModelSelectionType) {
        mUiLauncher = uiLauncher;

        if (productModelSelectionType != null) {
            mProductModelSelectionType = productModelSelectionType;
            if (productModelSelectionType.getHardCodedProductList().length == 0)
                throw new IllegalStateException("Please make sure to set valid CTN before invoke");
        } else
            throw new IllegalArgumentException("Please make sure to set the valid " +
                    "ProductModelSelectionType object");

        if (uiLauncher instanceof ActivityLauncher) {

            DigiCareLogger.i(TAG, "Launching as Activity");
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            themeConfiguration = activityLauncher.getDlsThemeConfiguration();
            invokeDigitalCareAsActivity(uiLauncher.getEnterAnimation(),
                    uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
            DLS_THEME = ((ActivityLauncher) uiLauncher).getUiKitTheme();
            /*  DigiCareLogger.i("testing", "DigitalCare Config -- Activity Invoke");*/

        } else {
            DigiCareLogger.i(TAG, "Launching through Fragment Manager instance");
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeDigitalCareAsFragment(fragmentLauncher.getFragmentActivity(),
                    fragmentLauncher.getParentContainerResourceID(),
                    fragmentLauncher.getActionbarListener(),
                    uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
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
    protected void invokeDigitalCareAsActivity(int startAnimation, int endAnimation,
                                               ActivityLauncher.ActivityOrientation
                                                       orientation) {
        if (mContext == null) {
            throw new RuntimeException("Please initialise context, " +
                    " and locale before Support page is invoked");
        }
        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.isEmpty() || (mAppName == null) ||
                    (mAppName.isEmpty()) || (mPageName == null) || (mPageName.isEmpty())) {
                throw new RuntimeException("Please make sure to set the valid " +
                        "App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }

        Intent intent = new Intent(this.getContext(), DigitalCareActivity.class);
        intent.putExtra(DigitalCareConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(DigitalCareConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(DigitalCareConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }

    public String getPreviousPageNameForTagging() {
        return mPageName;
    }

    @SuppressWarnings("deprecation")
    public AppInfraInterface getAPPInfraInstance() {
        return mAppInfraInterface;
    }

    public AppTaggingInterface getTaggingInterface() {
        AppTaggingInterface taggingInterface =
                getAPPInfraInstance().getTagging().createInstanceForComponent
                        (AnalyticsConstants.COMPONENT_NAME_CC, com.philips.cdp.digitalcare.BuildConfig.VERSION_NAME);

        return taggingInterface;
    }

    public LoggingInterface getLoggerInterface() {

        LoggingInterface loggingInterface = null;
        AppInfraInterface appInfraInstance = getAPPInfraInstance();
        if (appInfraInstance != null && appInfraInstance.getLogging() != null) {
            loggingInterface = appInfraInstance.getLogging().
                    createInstanceForComponent(AnalyticsConstants.COMPONENT_NAME_CC, com.philips.cdp.digitalcare.BuildConfig.VERSION_NAME);
        }
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


    public void registerCcListener(CcListener mainMenuListener) {
        mCcListener = mainMenuListener;
    }

    /**
     * Removing the {@link CcListener} object.
     *
     * @param mainMenuListener MainMenuListener interface object.
     */
    public void unRegisterCcListener(CcListener mainMenuListener) {
        mCcListener = null;
    }

    /**
     * Returns {@link CcListener} object.
     *
     * @return Returns the MainMenuListener object using across the DigitalCare component.
     */
    public CcListener getCcListener() {
        return mCcListener;
    }

    public ViewProductDetailsModel getViewProductDetailsData() {
        return mProductDetailsModel;
    }

    public void setViewProductDetailsData(ViewProductDetailsModel detailsObject) {
        mProductDetailsModel = detailsObject;
    }

    public ProductModelSelectionType getProductModelSelectionType() {
        return mProductModelSelectionType;
    }

    public String getLiveChatUrl() {
        return liveChatUrl;
    }

    public void setLiveChatUrl(String liveChatUrl) {
        this.liveChatUrl = liveChatUrl;
    }


    public String getSubCategoryUrl() {
        return subCategoryUrl;
    }

    public void setSubCategoryUrl(String subCategoryUrl) {
        this.subCategoryUrl = subCategoryUrl;
    }

    public String getCdlsUrl() {
        return cdlsUrl;
    }

    public void setCdlsUrl(String cdlsUrl) {
        this.cdlsUrl = cdlsUrl;
    }

    public String getAtosUrl() {
        return atosUrl;
    }

    public void setAtosUrl(String atosUrl) {
        this.atosUrl = atosUrl;
    }

    public String getEmailUrl() {
        return emailUrl;
    }

    public void setEmailUrl(String emailUrl) {
        this.emailUrl = emailUrl;
    }

    public String getProductReviewUrl() {
        return productReviewUrl;
    }

    public void setProductReviewUrl(String productReviewUrl) {
        this.productReviewUrl = productReviewUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getTwitterUrl() {
        return twitterUrl;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }


    public String getSdLiveChatUrl() {
        return sdLiveChatUrl;
    }

    public void setSdLiveChatUrl(String sdLiveChatUrl) {
        this.sdLiveChatUrl = sdLiveChatUrl;
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public int getDlsTheme() {
        return DLS_THEME;
    }

}
