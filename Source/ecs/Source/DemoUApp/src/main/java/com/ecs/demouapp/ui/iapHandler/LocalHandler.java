/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;


import com.ecs.demouapp.ui.integration.IAPInterface;
import com.ecs.demouapp.ui.integration.IAPListener;

public class LocalHandler extends IAPInterface implements IAPExposedAPI {

    @Override
    public void getProductCartCount(final IAPListener iapListener) {
        iapListener.onGetCartCount(-1);
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }
}