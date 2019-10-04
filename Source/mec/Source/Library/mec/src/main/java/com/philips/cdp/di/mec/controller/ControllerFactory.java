/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.controller;

import android.content.Context;


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

    /*public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.ShoppingCartListener<?> listener) {
        ShoppingCartAPI api;
        if (mIsPlanB) {
            api = new LocalShoppingCartPresenter(context, listener);
        } else {
            api = new ShoppingCartPresenter(context, listener);
        }
        return api;
    }

    public ProductCatalogAPI getProductCatalogPresenter(Context context,
                                                        ProductCatalogPresenter.ProductCatalogListener listener) {
        ProductCatalogAPI api;
        if (mIsPlanB)
            api = new LocalProductCatalog(context, listener);
        else {
            api = new ProductCatalogPresenter(context, listener);
        }
        return api;
    }*/
}