package com.philips.cdp.di.iap.Fragments;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;

public interface ProductDetailListener {
    public void onProductSelected(ShoppingCartData product, String extra);
}
