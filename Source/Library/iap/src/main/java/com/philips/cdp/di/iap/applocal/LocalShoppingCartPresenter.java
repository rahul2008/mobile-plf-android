/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.core.AbstractShoppingCartPresenter;

/**
 * For local store, we just need retailers url and details.
 * Retailer request doesn't depend on store and implemented in abstract class
 */
public class LocalShoppingCartPresenter extends AbstractShoppingCartPresenter{
    public LocalShoppingCartPresenter(final Context context, final ShoppingCartPresenter.LoadListener listener, final FragmentManager fragmentManager) {
        super(context, listener, fragmentManager);
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
    public void addProductToCart(final Context context, final String productCTN, final IAPCartListener iapHandlerListener, final boolean isFromBuyNow) {

    }

    @Override
    public void getProductCartCount(final Context context, final IAPCartListener iapHandlerListener) {

    }

    @Override
    public void buyProduct(final Context context, final String ctnNumber, final IAPCartListener iapHandlerListener) {

    }
}
