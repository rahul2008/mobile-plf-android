/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.applocal.LocalProductCatalog;
import com.philips.cdp.di.iap.applocal.LocalShoppingCartPresenter;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;

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

    @SuppressWarnings({"rawtype", "unchecked"})
    public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.LoadListener listener) {
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
    }
}