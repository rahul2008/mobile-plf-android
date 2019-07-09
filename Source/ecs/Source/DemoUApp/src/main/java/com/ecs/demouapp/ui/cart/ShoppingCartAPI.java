/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.cart;

import android.content.Context;

public interface ShoppingCartAPI {
    void getCurrentCartDetails();

    void deleteProduct(ShoppingCartData summary);

    void updateProductQuantity(ShoppingCartData data, int count, int quantityStatus);

    void addProductToCart(Context context, String productCTN, ECSCartListener iapHandlerListener,

                          boolean isFromBuyNow);

    void getProductCartCount(Context context, ECSCartListener iapHandlerListener);

    void buyProduct(Context context, String ctnNumber, ECSCartListener iapHandlerListener);

    void getRetailersInformation(String ctn);

}
