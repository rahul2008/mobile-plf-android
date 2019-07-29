/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.cart;

import android.content.Context;

import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.products.Product;

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
    //Do nothing
    }

    @Override
    public void deleteProduct(final ShoppingCartData summary) {
    //Do nothing
    }

    @Override
    public void deleteProduct(EntriesEntity entriesEntity) {

    }

    @Override
    public void updateProductQuantity(EntriesEntity entriesEntity, int count) {

    }

    @Override
    public void updateProductQuantity(final ShoppingCartData data, final int count, final int quantityStatus) {
    //Do nothing
    }

    @Override
    public void addProductToCart(Context context, String productCTN, ECSCartListener iapHandlerListener, boolean isFromBuyNow) {
    //Do nothing
    }

    @Override
    public void getProductCartCount(Context context, ECSCartListener iapHandlerListener) {
    //Do nothing
    }

    @Override
    public void addProductToCart(Product product, ECSCartListener iapHandlerListener) {
    //Do nothing
    }

}
