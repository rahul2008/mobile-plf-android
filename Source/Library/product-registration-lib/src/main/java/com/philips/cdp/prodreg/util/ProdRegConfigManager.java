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

package com.philips.cdp.prodreg.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.fragments.InitialFragment;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.ui.ActivityLauncher;
import com.philips.cdp.prodreg.ui.FragmentLauncher;
import com.philips.cdp.prodreg.ui.UiLauncher;

public class ProdRegConfigManager {

    private static final String TAG = ProdRegConfigManager.class.getSimpleName();
    private static Context mContext = null;
    private static ProdRegConfigManager prodRegConfigManager;
    private String mAppID = null;
    private String mAppName = null;
    private String mPageName = null;
    private boolean mTaggingEnabled = false;
    private UiLauncher mUiLauncher;

    /*
     * Initialize everything(resources, variables etc) required for DigitalCare.
     * Hosting app, which will integrate this DigitalCare, has to pass app
     * context.
     */
    private ProdRegConfigManager() {
    }

    /*
     * Singleton pattern.
     */
    public static ProdRegConfigManager getInstance() {
        if (prodRegConfigManager == null) {
            prodRegConfigManager = new ProdRegConfigManager();
        }
        return prodRegConfigManager;
    }

    private static void initializeTaggingContext(Context context) {
//        AnalyticsTracker.initContext(context);
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
     * <p> Invoking ProductRegistration component features to your Fragment Container. Please use this
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
    private void invokeProductRegistrationAsFragment(FragmentActivity context,
                                                     int parentContainerResId,
                                                     ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                                     int exitAnim) {

        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("") || (mAppName == null) || (mAppName == "") || (mPageName == null) || (mPageName == "")) {
                throw new RuntimeException("Please make sure to set the valid App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }

//        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        FragmentLauncher fragmentLauncher = new FragmentLauncher(context, parentContainerResId,
                actionbarUpdateListener);

        InitialFragment supportFrag = new InitialFragment();
        supportFrag.showFragment(supportFrag,
                fragmentLauncher, enterAnim, exitAnim);
    }

    public void invokeProductRegistration(UiLauncher uiLauncher) {
        mUiLauncher = uiLauncher;
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            invokeProductRegistrationAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeProductRegistrationAsFragment(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID(),
                    fragmentLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
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
    private void invokeProductRegistrationAsActivity(int startAnimation, int endAnimation, ActivityLauncher.ActivityOrientation orientation) {
        if (mContext == null) {
            throw new RuntimeException("Please initialise context before Support page is invoked");
        }
        if (mTaggingEnabled) {
            if (mAppID == null || mAppID.equals("") || (mAppName == null) || (mAppName == "") || (mPageName == null) || (mPageName == "")) {
                throw new RuntimeException("Please make sure to set the valid App Tagging inputs by invoking setAppTaggingInputs API");
            }
        }

//        AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

        Intent intent = new Intent(this.getContext(), ProdRegBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ProdRegConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(ProdRegConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(ProdRegConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
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
}
