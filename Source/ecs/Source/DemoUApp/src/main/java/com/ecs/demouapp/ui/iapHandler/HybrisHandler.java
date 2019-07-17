/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;

import android.content.Context;
import android.os.Message;

import com.ecs.demouapp.ui.cart.ECSCartListener;
import com.ecs.demouapp.ui.cart.ShoppingCartAPI;
import com.ecs.demouapp.ui.cart.ShoppingCartPresenter;
import com.ecs.demouapp.ui.controller.ControllerFactory;
import com.ecs.demouapp.ui.integration.ECSInterface;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.products.ProductCatalogAPI;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.session.RequestListener;
import com.ecs.demouapp.ui.utils.ECSConstant;


public class HybrisHandler extends ECSInterface implements ECSExposedAPI {
    private Context mContext;

    public HybrisHandler(Context context) {
        mContext = context;
    }

    @Override
    public void getProductCartCount(final ECSListener iapListener) {
        if (isStoreInitialized()) {
            getProductCount(iapListener);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            getProductCount(iapListener);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

    private void getProductCount(final ECSListener iapListener) {
        ShoppingCartAPI presenter = new ShoppingCartPresenter();
        presenter.getProductCartCount(mContext, new ECSCartListener() {
            @Override
            public void onSuccess(final int count) {
                iapListener.onGetCartCount(count);
            }

            @Override
            public void onFailure(final Message msg) {
                iapListener.onFailure(getIAPErrorCode(msg));
            }
        });
    }

    @Override
    public void getCompleteProductList(final ECSListener iapListener) {
        final ProductCatalogAPI presenter =
                ControllerFactory.getInstance().getProductCatalogPresenter();
        if (isStoreInitialized()) {
            presenter.getCompleteProductList(iapListener);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(/*mLanguage, mCountry,*/ new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            presenter.getCompleteProductList(iapListener);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

    public int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return ECSConstant.IAP_ERROR_UNKNOWN;
    }

    public boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }
}
