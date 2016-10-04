/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.applocal.LocalProductCatalog;
import com.philips.cdp.di.iap.applocal.LocalShoppingCartPresenter;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;

public class ControllerFactory {
    private static ControllerFactory mController = new ControllerFactory();
    private int mRequestCode;

    /**
     * Must be called only from IAPHandler.
     */
    public void init(int requestCode) {
        mRequestCode = requestCode;
    }

    public static ControllerFactory getInstance() {
        return mController;
    }

    public boolean shouldDisplayCartIcon() {
        return !loadLocalData();
    }

    public boolean loadLocalData() {
        return mRequestCode == NetworkEssentialsFactory.LOAD_LOCAL_DATA;
    }

    @SuppressWarnings({"rawtype","unchecked"})
    public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.LoadListener listener,
                                                    FragmentManager fragmentManager) {
        ShoppingCartAPI api;
        boolean isLocalData = loadLocalData();
        if (isLocalData) {
            api = new LocalShoppingCartPresenter(context, listener, fragmentManager);
        }
        else {
            api = new ShoppingCartPresenter(context, listener, fragmentManager);
        }
        return api;
    }

    public ProductCatalogAPI getProductCatalogPresenter(Context context,
                                                        ProductCatalogPresenter.ProductCatalogListener listener) {
        ProductCatalogAPI api;
        if (loadLocalData())
            api = new LocalProductCatalog(context, listener);
        else {
            api = new ProductCatalogPresenter(context, listener);
        }
        return api;
    }
}