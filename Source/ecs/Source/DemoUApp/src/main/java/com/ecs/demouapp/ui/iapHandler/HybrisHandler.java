/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;

import com.ecs.demouapp.ui.integration.ECSInterface;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;


public class HybrisHandler extends ECSInterface implements ECSExposedAPI {

    @Override
    public void getProductCartCount(final ECSListener iapListener) {

        ECSUtility.getInstance().getEcsServices().getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart carts) {

                if (carts != null) {
                    int quantity = ECSUtility.getInstance().getQuantity(carts);
                    iapListener.onGetCartCount(quantity);
                } else {
                    iapListener.onFailure(5999);
                }
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                iapListener.onFailure(ecsError.getErrorcode());
            }
        });
    }

    @Override
    public void getCompleteProductList(final ECSListener iapListener) {

            ECSUtility.getInstance().getEcsServices().getProductList(0, 20, new ECSCallback<Products, Exception>() {
                @Override
                public void onResponse(Products result) {

                    ArrayList<String> ctns = new ArrayList<>();

                    for(Product product:result.getProducts()){
                        ctns.add(product.getCode());
                    }
                    iapListener.onGetCompleteProductList(ctns);
                }

                @Override
                public void onFailure(Exception error, ECSError ecsError) {

                }
            });
    }

}
