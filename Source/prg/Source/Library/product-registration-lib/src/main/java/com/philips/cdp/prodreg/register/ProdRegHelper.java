/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    private ProdRegListener prodRegListener;

    @NonNull
    private static UserRegistrationUIEventListener getUserRegistrationListener() {
        return new UserRegistrationUIEventListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                if (activity != null && activity instanceof RegistrationActivity) {
                    activity.finish();
                }
                final User user = new User(activity);
                final ProdRegListener prodRegListener = new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                    }

                    @Override
                    public void onProdRegFailed(final RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                    }
                };
                new ProdRegHelper().addProductRegistrationListener(prodRegListener);
                new UserWithProducts(activity, user, prodRegListener).registerCachedProducts(new LocalRegisteredProducts(user).getRegisteredProducts());
            }

            @Override
            public void onPrivacyPolicyClick(final Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(final Activity activity) {

            }
        };
    }

    /**
     * API to be called to initialize product registration
     */
    public void init() {
        UserRegistrationObserver.registerListerOnUserSignIn();
    }

    /**
     * API to add listener while registering product
     *
     * @param listener - Pass listener instance to listen for call backs
     */
    public void addProductRegistrationListener(final ProdRegListener listener) {
        this.prodRegListener = listener;
    }

    public void removeProductRegistrationListener(final ProdRegListener prodRegListener) {
//        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(userRegistrationListener);
    }

    /**
     * API which returns UserWithProducts instance for current signed in user
     *
     * @return - returns instance of UserWithProducts
     */
    public UserWithProducts getSignedInUserWithProducts(Context context) {
        return new UserWithProducts(context, new User(context), prodRegListener);
    }

    /**
     * API will return the Build Version
     *
     * @return version name in string
     */
    public String getLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    private static class UserRegistrationObserver {
        protected static void registerListerOnUserSignIn() {
            final URLaunchInput urLaunchInput = new URLaunchInput();
           // urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
            urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
            urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
            urLaunchInput.setUserRegistrationUIEventListener(getUserRegistrationListener());
        }
    }

}
