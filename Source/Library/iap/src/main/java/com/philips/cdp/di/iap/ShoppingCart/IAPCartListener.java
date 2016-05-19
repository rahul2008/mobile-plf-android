package com.philips.cdp.di.iap.ShoppingCart;

import android.os.Message;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPCartListener {
    /**
     * If called with addProductToCart, returns the cart items count
     * Otherwise always returns 0
     * @param count Items in cart
     */
    void onSuccess(int count);

    /**
     * Error occurred during the request
     * @param msg
     */
    void onFailure(final Message msg);
}
