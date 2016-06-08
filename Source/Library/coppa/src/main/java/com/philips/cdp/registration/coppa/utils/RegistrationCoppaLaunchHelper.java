/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.registration.coppa.ui.Activity.RegistrationCoppaActivity;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class RegistrationCoppaLaunchHelper {

    public static void launchDefaultRegistrationActivity(Context context) {
        launchDefaultRegistrationMode(context);
    }

    public static void launchDefaultRegistrationActivityWithFixedOrientation(Context context, int orientation) {
        launchFixedOrientationRegistrationActivity(context, orientation);
    }

    public static void launchParentalConsent(Context context){
        launchParentalConsentScreen(context);

    }

    public static void launchRegistrationActivityWithAccountSettings(Context context) {
        launchDefaultRegistrationMode(context);
    }

    public static void launchRegistrationActivityWithFixedOrientationWithAccountSettings(Context context, int orientation) {
        launchFixedOrientationRegistrationActivity(context, orientation);
    }

    public static void launchRegistrationActivityWithOutAccountSettings(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationCoppaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    public static void launchRegistrationActivityWithFixedOrientationWithOutAccountSettings(Context context, int orientation) {
        Intent registrationIntent = new Intent(context, RegistrationCoppaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }


    public static boolean isBackEventConsumedByRegistration(FragmentActivity fragmentActivity) {

        //true consimned
        //false not consumed
        if(fragmentActivity != null) {
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            Fragment fragment = fragmentManager
                    .findFragmentByTag(RegConstants.REGISTRATION_COPPA_FRAGMENT_TAG);
            if (fragment != null) {
                return ((RegistrationCoppaFragment) fragment).onBackPressed();
            }
        }
        return true;
    }

    private static void launchDefaultRegistrationMode(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationCoppaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    private static void launchFixedOrientationRegistrationActivity(Context context, int orientation) {
        Intent registrationIntent = new Intent(context, RegistrationCoppaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    private static void launchParentalConsentScreen(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationCoppaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        bundle.putBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, true);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }


    public void registerUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().registerEventNotification(userRegistrationListener);
    }

    public void unRegisterUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().unregisterEventNotification(userRegistrationListener);
    }


}
