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
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.ArrayList;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    public static ArrayList<ProdRegListener> prodRegListeners;
    private static Context context;
    private static UserRegistrationListener userRegistrationListener;
    private static ProdRegHelper prodRegHelper;
    private ProdRegListener prodRegListener;

    @NonNull
    private static UserRegistrationListener getUserRegistrationListener() {
        userRegistrationListener = new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                final User user = new User(context);
                final ProdRegListener prodRegListener = new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                    }

                    @Override
                    public void onProdRegFailed(final RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                    }
                };
                new ProdRegHelper().addProductRegistrationListener(prodRegListener);
                new UserWithProducts(context, user, prodRegListener).registerCachedProducts(new LocalRegisteredProducts(activity, user).getRegisteredProducts());
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
        };
        return userRegistrationListener;
    }

 /*   public static ProdRegHelper getInstance() {
        if (prodRegHelper == null) {
            prodRegHelper = new ProdRegHelper();
        }
        return prodRegHelper;
    }*/

    /**
     * API to be called to initialize product registration
     *
     * @param context - Application context
     */
    public void init(Context context) {
        ProdRegHelper.context = context;
        ProdRegHelper.prodRegListeners = new ArrayList<>();
        UserRegistrationObserver.registerListerOnUserSignIn();
    }

    /**
     * API to add listener while registering product
     *
     * @param listener - Pass listener instance to listen for call backs
     */
    public void addProductRegistrationListener(final ProdRegListener listener) {
        this.prodRegListener = listener;
        prodRegListeners.add(listener);
    }

    public void removeProductRegistrationListener(final ProdRegListener prodRegListener) {
//        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(userRegistrationListener);
        prodRegListeners.remove(prodRegListener);
    }

    /**
     * API which returns UserWithProducts instance for current signed in user
     *
     * @return - returns instance of UserWithProducts
     */
    public UserWithProducts getSignedInUserWithProducts() {
        final UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener);
        return userWithProducts;
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
            RegistrationHelper.getInstance().registerUserRegistrationListener(getUserRegistrationListener());
        }
    }
}
