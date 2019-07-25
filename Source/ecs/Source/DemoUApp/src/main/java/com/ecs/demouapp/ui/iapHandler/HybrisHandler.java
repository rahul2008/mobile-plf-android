/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;

import android.os.Message;

import com.ecs.demouapp.ui.controller.ControllerFactory;
import com.ecs.demouapp.ui.integration.ECSInterface;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.products.ProductCatalogAPI;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;

import java.util.List;


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
                    iapListener.onFailure(9000);
                }
            }

            @Override
            public void onFailure(Exception error,String detailErrorMessage, int errorCode) {
                iapListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getCompleteProductList(final ECSListener iapListener) {
        final ProductCatalogAPI presenter =
                ControllerFactory.getInstance().getProductCatalogPresenter();
            presenter.getCompleteProductList(iapListener);
    }

}
