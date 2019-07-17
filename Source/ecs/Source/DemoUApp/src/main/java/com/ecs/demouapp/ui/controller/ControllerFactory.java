/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;

import com.ecs.demouapp.ui.cart.LocalShoppingCartPresenter;
import com.ecs.demouapp.ui.cart.ShoppingCartAPI;
import com.ecs.demouapp.ui.cart.ShoppingCartPresenter;
import com.ecs.demouapp.ui.products.LocalProductCatalog;
import com.ecs.demouapp.ui.products.ProductCatalogAPI;
import com.ecs.demouapp.ui.products.ProductCatalogPresenter;


public class ControllerFactory {
    private static ControllerFactory mController = new ControllerFactory();
    private boolean mIsPlanB;

    public static ControllerFactory getInstance() {
        return mController;
    }

    public void init(boolean isLocalData) {
        mIsPlanB = isLocalData;
    }

    public boolean isPlanB() {
        return mIsPlanB;
    }

    public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.ShoppingCartListener<?> listener) {
        ShoppingCartAPI api;
        if (mIsPlanB) {
            api = new LocalShoppingCartPresenter(context, listener);
        } else {
            api = new ShoppingCartPresenter(context, listener);
        }
        return api;
    }

    public ProductCatalogAPI getProductCatalogPresenter() {
        ProductCatalogAPI api;
//        if (mIsPlanB)
//            api = new LocalProductCatalog(context, listener);
//        else {
            api = new ProductCatalogPresenter();
        //}
        return api;
    }
}