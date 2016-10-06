/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.cart;

import android.content.Context;

/**
 * For local store, we just need retailers url and details.
 * Retailer request doesn't depend on store and implemented in abstract class
 */
public class LocalShoppingCartPresenter extends AbstractShoppingCartPresenter{

    @SuppressWarnings("rawtypes")
    public LocalShoppingCartPresenter(Context context, ShoppingCartListener listener) {
        super(context, listener);
    }

    @Override
    public void getCurrentCartDetails() {

    }

    @Override
    public void deleteProduct(final ShoppingCartData summary) {

    }

    @Override
    public void updateProductQuantity(final ShoppingCartData data, final int count, final int quantityStatus) {

    }

    @Override
    public void addProductToCart(Context context, String productCTN, IAPCartListener iapHandlerListener,  boolean isFromBuyNow) {

    }

    @Override
    public void getProductCartCount(Context context, IAPCartListener iapHandlerListener) {

    }

    @Override
    public void buyProduct(Context context, String ctnNumber, IAPCartListener iapHandlerListener) {

    }
}
