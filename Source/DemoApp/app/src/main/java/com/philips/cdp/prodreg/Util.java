package com.philips.cdp.prodreg;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class Util {

    public static void navigateFromUserRegistration() {
        RegistrationHelper.getInstance().registerUserRegistrationListener(new UserRegistrationListener() {
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
