/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;


import java.util.ArrayList;

/**
 * Vertical needs to implement IAPListener to handle visibility of cart icon, updating cart count, on update cart count, on get complete product list, on success and on failure.
 * @since 1.0.0
 */
public interface MECListener {

    /**
     * Gets the product count in cart
     * @param count - count is an integer through which basis user can update the count visibility
     * @since 1.0.0
     */
    void onGetCartCount(int count);

    /**
     * Notifies when product count in cart is updated
     * @since 1.0.0
     */
    void onUpdateCartCount();

    /**
     * Notifies true for cart icon visibility or false for hide
     * @param shouldShow  boolean will help to update hte cart icon visibility
     * @since 1.0.0
     */
    void updateCartIconVisibility(boolean shouldShow);

    /**
     * Notifies true for when fetched complet product form backend service or false if not fetched
     * @param productList  will get list of CTNs from backend
     * @since 1.0.0
     */
    void onGetCompleteProductList(final ArrayList<String> productList);

    /**
     * Notifies success of any request
     * @since 1.0.0
     */
    void onSuccess();

    /**
     * Notifies true for backend response or false for local
     * @param bool  will get true or false
     * @since 1.0.0
     */
    void onSuccess(boolean bool);

    /**
     * It gives failure responses with error code
     * @param errorCode   errorCode when IAP encounters any error
     * @since 1.0.0
     */
    void onFailure(final int errorCode);

}
