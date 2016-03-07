package com.philips.cdp.di.iap.session;

import android.os.Message;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPHandlerListner {

    void onGetCartQuantity(int quantity);

    void onAddItemToCart(Message mgs);

    void onBuyNow();
}
