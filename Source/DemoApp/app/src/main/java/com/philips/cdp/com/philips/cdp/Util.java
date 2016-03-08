package com.philips.cdp.com.philips.cdp;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Util {

    public static void navigateFromUserRegistration() {
        RegistrationHelper.getInstance().getUserRegistrationListener().registerEventNotification(new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                activity.finish();
            }

            @Override
            public void onPrivacyPolicyClick(final Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(final Activity activity) {

            }

            @Override
            public void onUserLogoutSuccess() {

            }

            @Override
            public void onUserLogoutFailure() {

            }

            @Override
            public void onUserLogoutSuccessWithInvalidAccessToken() {

            }
        });
    }
}
