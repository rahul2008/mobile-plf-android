/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;


import com.ecs.demouapp.ui.integration.ECSInterface;
import com.ecs.demouapp.ui.integration.ECSListener;

public class LocalHandler extends ECSInterface implements ECSExposedAPI {

    @Override
    public void getProductCartCount(final ECSListener iapListener) {
        iapListener.onGetCartCount(-1);
    }

    @Override
    public void getCompleteProductList(ECSListener iapListener) {
    }
}