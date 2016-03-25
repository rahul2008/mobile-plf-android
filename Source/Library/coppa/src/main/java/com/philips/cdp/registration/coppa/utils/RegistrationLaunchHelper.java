package com.philips.cdp.registration.coppa.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.coppa.ui.Activity.RegistrationActivity;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

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


    public static boolean isBackEventConsumedByRegistration(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentByTag(RegConstants.REGISTRATION_FRAGMENT_TAG);
        if (fragment != null) {

            try {
                if (((RegistrationCoppaFragment) fragment).onBackPressed()) {
                    return false;
                }
            } catch (ClassCastException e) {
                RLog.d("known exception", e.toString());



                if (((RegistrationFragment) fragment).onBackPressed()) {


                    //true for restricting Login @ login screen need to think and do

                      //  return false;


                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    // return true;


                    // mFragmentManager.popBackStack();


                    // return false;
                }
            }


        }
        return true;
    }

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
