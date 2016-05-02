/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    private static Context context;
    private static ProdRegListener prodRegListener;
    private static UserRegistrationListener userRegistrationListener;
    private String locale;

    @NonNull
    private static UserRegistrationListener getUserRegistrationListener() {
        userRegistrationListener = new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                final User user = new User(context);
                new UserWithProducts(context, new User(context), prodRegListener).registerCachedProducts(new LocalRegisteredProducts(activity, user).getRegisteredProducts(), new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Log.d("Product Registration logs ", "Product " + registeredProduct.getCtn() + " and Serial " + registeredProduct.getSerialNumber() + " registered successfully");
                    }

                    @Override
                    public void onProdRegFailed(final RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Log.d("Product Registration logs ", "Product " + registeredProduct.getCtn() + " and Serial " + registeredProduct.getSerialNumber() + " failed due to " + registeredProduct.getProdRegError());
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
        };
        return userRegistrationListener;
    }

    /**
     * API to be called to initialize product registration
     *
     * @param context
     */
    public void init(Context context) {
        ProdRegHelper.context = context;
        UserRegistrationObserver.registerListerOnUserSignIn();
    }

    /**
     * API to set locale
     * @param language - language code
     * @param countryCode - country code
     */
    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    /**
     * API which returns locale
     * @return - returns locale of type String
     */
    public String getLocale() {
        return locale;
    }

    /**
     * API to add listener while registering product
     * @param listener
     */
    public void addProductRegistrationListener(final ProdRegListener listener) {
        prodRegListener = listener;
    }

    public void removeProductRegistrationListener() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(userRegistrationListener);
    }

    /**
     * API which returns UserWithProducts instance for current signed in user
     * @return
     */
    public UserWithProducts getSignedInUserWithProducts() {
        final UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener);
        userWithProducts.setLocale(this.locale);
        return userWithProducts;
    }

    private static class UserRegistrationObserver {
        protected static void registerListerOnUserSignIn() {
            RegistrationHelper.getInstance().registerUserRegistrationListener(getUserRegistrationListener());
        }
    }
}
