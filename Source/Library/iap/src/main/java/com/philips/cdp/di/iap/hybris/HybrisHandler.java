/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class HybrisHandler extends IAPInterface implements IAPExposedAPI {
    private Context mContext;
    private String mLanguage;
    private String mCountry;

    private static final int CURRENT_PAGE = 0;
    private static final int PAGE_SIZE = 10;


    public HybrisHandler(Context context) {
        mContext = context;
        mLanguage = CartModelContainer.getInstance().getLanguage();
        mCountry = CartModelContainer.getInstance().getCountry();
    }

    @Override
    public void getProductCartCount(final IAPListener iapListener) {
        if (isStoreInitialized()) {
            getProductCount(iapListener);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
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

    private void getProductCount(final IAPListener iapListener) {
        ShoppingCartAPI presenter = new ShoppingCartPresenter();
        presenter.getProductCartCount(mContext, new IAPCartListener() {
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
    public void getCompleteProductList(final IAPListener iapHandlerListener) {
        final ProductCatalogAPI presenter =
                ControllerFactory.getInstance().getProductCatalogPresenter(mContext, null, null);
        if (isStoreInitialized()) {
            presenter.getCompleteProductList(mContext, iapHandlerListener, CURRENT_PAGE, PAGE_SIZE);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            presenter.getCompleteProductList(mContext, iapHandlerListener, CURRENT_PAGE, PAGE_SIZE);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapHandlerListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }

    private boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }
}
