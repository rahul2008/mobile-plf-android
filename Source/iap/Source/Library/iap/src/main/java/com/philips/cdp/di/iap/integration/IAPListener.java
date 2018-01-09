/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;


import java.util.ArrayList;

/**
 * It is used to send call backs to propositions
 */
public interface IAPListener {

    /**
     * gets the product count in cart
     * @param count - int count
     * @since 1.0.0
     */
    void onGetCartCount(int count);

    /**
     * notifies when product count in cart is updated
     * @since 1.0.0
     */
    void onUpdateCartCount();

    /**
     * determines whether to show cart or not
     * @param shouldShow - boolean shouldShow
     * @since 1.0.0
     */
    void updateCartIconVisibility(boolean shouldShow);

    /**
     * notifies when complete product list is fetched
     * @param productList - ArrayList<String> productList
     * @since 1.0.0
     */
    void onGetCompleteProductList(final ArrayList<String> productList);

    /**
     * notifies success of any request
     * @since 1.0.0
     */
    void onSuccess();

    /**
     * notifies if cart is enabled or not from service discovery
     * @param bool - boolean bool
     * @since 1.0.0
     */
    void onSuccess(boolean bool);

    /**
     * it gives failure responses with error code
     * @param errorCode - int errorCode
     * @since 1.0.0
     */
    void onFailure(final int errorCode);

}
