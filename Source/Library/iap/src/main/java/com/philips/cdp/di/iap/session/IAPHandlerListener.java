/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import java.util.ArrayList;

public interface IAPHandlerListener {

    /**
     * If called with addProductToCart, returns the cart items count
     * Otherwise always returns 0
     *
     * @param count Items in cart
     */
    void onSuccess(int count);

    /**
     * Error occurred during the request
     *
     * @param errorCode
     */
    void onFailure(final int errorCode);

    /**
     * Product list from Hybris request
     *
     * @param productList
     */
    void onFetchOfProductList(final ArrayList<String> productList);
}
