/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

public class RegistrationLaunchHelper {

    public static void launchDefaultRegistrationActivity(Context context) {
        launchDefaultRegistrationMode(context);
    }

    public static void launchDefaultRegistrationActivityWithFixedOrientation(Context context, int orientation) {
        launchFixedOrientationRegistrationActivity(context, orientation);
    }

    public static void launchRegistrationActivityWithAccountSettings(Context context) {
        launchDefaultRegistrationMode(context);
    }

    public static void launchRegistrationActivityWithFixedOrientationWithAccountSettings(Context context, int orientation) {
        launchFixedOrientationRegistrationActivity(context, orientation);
    }

    public static void launchRegistrationActivityWithOutAccountSettings(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    public static void launchRegistrationActivityWithFixedOrientationWithOutAccountSettings(Context context, int orientation) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }


/*    public static boolean isBackEventConsumedByRegistration(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.fl_reg_fragment_container);
        if (fragment != null) {
            if (((RegistrationFragment) fragment).onBackPressed()) {
                return false;
            }
        }
        return true;
    }*/

    private static void launchDefaultRegistrationMode(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    private static void launchFixedOrientationRegistrationActivity(Context context, int orientation) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }
}
