package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.AppTagging.AppTagging;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;

public class RegistrationLaunchHelper {

    public static void launchRegistrationActivity(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        context.startActivity(registrationIntent);
    }

    public static void launchRegistrationActivityWithFixedOrientation(Context context, int orientation) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }

    /**
     * Launch registration fragment
     */
    public static void launchRegistrationFragment(int container, FragmentActivity fragmentActivity, RegistrationTitleBarListener titleBarListener) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            registrationFragment.setOnUpdateTitleListener(titleBarListener);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }


    public static boolean isBackEventConsumedByRegistration(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentByTag(RegConstants.REGISTRATION_FRAGMENT_TAG);
        if (fragment != null) {
            if (((RegistrationFragment) fragment).onBackPressed()) {
                return false;
            }
        }
        return true;
    }

}
