package com.philips.cdp.di.iap.session;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPHandlerListner {

    void onGetCartQuantity(int quantity);

    void onAddItemToCart(String responseStatus);
}
