/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.handler;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.prodreg.backend.LocalRegisteredProducts;
import com.philips.cdp.prodreg.backend.RegisteredProduct;
import com.philips.cdp.prodreg.backend.UserProduct;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    private String locale;
    private Context context;
    private ProdRegListener prodRegListener;
    private UserRegistrationListener userRegistrationListener;
    private UserProduct userProduct;

    public void init(Context context) {
        this.context = context;
        userProduct = new UserProduct(context, new User(context));
        registerListerOnUserSignIn();
    }

    @NonNull
    UserProduct getUserProduct() {
        return userProduct;
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    public String getLocale() {
        return locale;
    }

    public void addProductRegistrationListener(final ProdRegListener listener) {
        this.prodRegListener = listener;
    }

    private void registerListerOnUserSignIn() {
        RegistrationHelper.getInstance().registerUserRegistrationListener(getUserRegistrationListener());
    }

    @NonNull
    private UserRegistrationListener getUserRegistrationListener() {
        userRegistrationListener = new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                final User user = new User(activity);
                new UserProduct(activity, new User(context)).registerCachedProducts(new LocalRegisteredProducts(activity, user).getRegisteredProducts(), new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct) {
                        Log.d("Product Registration logs ", "Product " + registeredProduct.getCtn() + " and Serial " + registeredProduct.getSerialNumber() + " registered successfully");
                    }

                    @Override
                    public void onProdRegFailed(final RegisteredProduct registeredProduct) {
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

    public void removeProductRegistrationListener() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(userRegistrationListener);
    }

    public UserProduct getSignedInUser() {
        UserProduct userProduct = getUserProduct();
        userProduct.setProductRegistrationListener(prodRegListener);
        userProduct.setLocale(this.locale);
        return userProduct;
    }

    public void getRegisteredProducts(final RegisteredProductsListener registeredProductsListener) {
        userProduct.getRegisteredProducts(registeredProductsListener);
    }
}
