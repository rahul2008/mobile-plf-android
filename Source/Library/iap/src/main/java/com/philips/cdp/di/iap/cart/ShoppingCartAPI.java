/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.cart;

import android.content.Context;

import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartData;

public interface ShoppingCartAPI {
    void getCurrentCartDetails();

    void deleteProduct(ShoppingCartData summary);

    void updateProductQuantity(ShoppingCartData data, int count, int quantityStatus);

    void addProductToCart(Context context, String productCTN, IAPCartListener iapHandlerListener,

                          boolean isFromBuyNow);

    void getProductCartCount(Context context, IAPCartListener iapHandlerListener);

    void buyProduct(Context context, String ctnNumber, IAPCartListener iapHandlerListener);

    void getRetailersInformation(String ctn);
}
