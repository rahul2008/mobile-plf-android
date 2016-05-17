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
     * Must not be called from outside.
     */
    public void init(int requestCode) {
        mRequestCode = requestCode;
    }

    public static ControllerFactory getInstance() {
        return mController;
    }

    public ShoppingCartAPI getShoppingCartPresenter(Context context,
                                                    ShoppingCartPresenter.LoadListener listener, FragmentManager fragmentManager) {
        ShoppingCartAPI api = null;
        if (mRequestCode == NetworkEssentialsFactory.LOAD_LOCAL_DATA) {
            //Still need to implement
        } else {
            api = new ShoppingCartPresenter(context, listener, fragmentManager);
        }
        return api;
    }
}
