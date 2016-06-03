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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.fragments.RegisterProductWelcomeFragment;
import com.philips.cdp.prodreg.launcher.ActivityLauncher;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.launcher.UiLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;

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

    public void initializeProductRegistration(Context applicationContext) {
        if (mContext == null) {
            ProdRegConfigManager.mContext = applicationContext;
        }
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
     * <p> 1) Please consider the string "product_registration" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
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
        User user = new User(context);

        if (user.isUserSignIn()) {
            RegisterProductWelcomeFragment supportFrag = new RegisterProductWelcomeFragment();
            supportFrag.showFragment(supportFrag,
                    fragmentLauncher, enterAnim, exitAnim);
        } else {
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithBack(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithOutBack(final int i) {

                }
            });
            FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(registrationFragment.getTag());
            fragmentTransaction.replace(parentContainerResId, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }

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
