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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.fragments.ProdRegFirstLaunchFragment;
import com.philips.cdp.prodreg.fragments.ProdRegProcessFragment;
import com.philips.cdp.prodreg.launcher.ActivityLauncher;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.launcher.UiLauncher;
import com.philips.cdp.prodreg.listener.ProdRegBackListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class ProdRegConfigManager {

    private static int mContainerId;
    private static ProdRegConfigManager prodRegConfigManager;
    private UiLauncher mUiLauncher;

    /*
     * Initialize everything(resources, variables etc) required for Product Registration.
     * Hosting app, which will integrate this Product Registration, has to pass app
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

    /**
     * <p> Invoking ProductRegistration component features to your Fragment Container. Please use this
     * method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "product_registration" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
     */
    private void invokeProductRegistrationAsFragment(FragmentLauncher fragmentLauncher) {
        mContainerId = fragmentLauncher.getParentContainerResourceID();
        final Context mContext = fragmentLauncher.getFragmentActivity().getApplicationContext();
        User user = new User(mContext);
        if (fragmentLauncher.isFirstLaunch()) {
            ProdRegFirstLaunchFragment prodRegFirstLaunchFragment = new ProdRegFirstLaunchFragment();
            prodRegFirstLaunchFragment.setArguments(fragmentLauncher.getArguments());
            prodRegFirstLaunchFragment.showFragment(prodRegFirstLaunchFragment,
                    fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
        } else if (!user.isUserSignIn()) {
            launchRegistrationFragment(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID());
        } else {
            ProdRegProcessFragment prodRegProcessFragment = new ProdRegProcessFragment();
            prodRegProcessFragment.setArguments(fragmentLauncher.getArguments());
            prodRegProcessFragment.showFragment(prodRegProcessFragment,
                    fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
        }
    }

    private void launchRegistrationFragment(final FragmentActivity context, final int parentContainerResId) {
        try {
            RegistrationFragment registrationFragment = new
                    RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
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
            FragmentTransaction fragmentTransaction =
                    context.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(parentContainerResId, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void invokeProductRegistration(UiLauncher uiLauncher) {
        mUiLauncher = uiLauncher;
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            invokeProductRegistrationAsActivity(activityLauncher.getFragmentActivity(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeProductRegistrationAsFragment(fragmentLauncher);
        }
    }

    public UiLauncher getUiLauncher() {
        return mUiLauncher;
    }

    /**
     * <p> Invoking Product Registration Component from the Intent. </p>
     * <b> Note: </b> Please make sure to set the Locale before invoking this method.
     *
     * @param startAnimation Animation resource ID.
     * @param endAnimation   Animation Resource ID.
     * @param orientation
     */
    private void invokeProductRegistrationAsActivity(Context context, int startAnimation, int endAnimation, ActivityLauncher.ActivityOrientation orientation) {
        Intent intent = new Intent(context, ProdRegBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ProdRegConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(ProdRegConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(ProdRegConstants.PROD_REG_ON_FIRST_LAUNCH, mUiLauncher.isFirstLaunch());
        intent.putExtra(ProdRegConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        context.startActivity(intent);
    }

    public boolean onBackPressed(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        if (fragmentManager != null && !fragmentActivity.isFinishing()) {
            Fragment currentFrag = fragmentManager
                    .findFragmentById(mContainerId);
            if (currentFrag != null && currentFrag instanceof ProdRegBackListener) {
                ((ProdRegBackListener) currentFrag).onBackPressed();
            }
        }
        return false;
    }
}
