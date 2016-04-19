package com.philips.cdp.prodreg;

import android.app.Activity;

import com.philips.cdp.prodreg.backend.LocalRegisteredProducts;
import com.philips.cdp.prodreg.backend.UserProduct;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prxclient.response.ResponseData;
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
                new UserProduct(activity).registerCachedProducts(new LocalRegisteredProducts(activity).getRegisteredProducts(), new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess() {

                    }

                    @Override
                    public void onProdRegFailed(final ProdRegError prodRegError) {

                    }
                });
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
