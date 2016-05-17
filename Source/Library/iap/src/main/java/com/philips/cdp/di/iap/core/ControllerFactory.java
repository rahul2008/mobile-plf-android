/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;

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
        if (loadLocalData()) {
            return false;
        }
        return true;
    }

    private boolean loadLocalData() {
        return mRequestCode == NetworkEssentialsFactory.LOAD_LOCAL_DATA;
    }

    public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.LoadListener listener, FragmentManager fragmentManager) {
        ShoppingCartAPI api = null;
        if (loadLocalData()) {
            //Still need to implement
        } else {
            api = new ShoppingCartPresenter(context, listener, fragmentManager);
        }
        return api;
    }
}