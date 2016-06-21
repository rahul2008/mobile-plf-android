/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.IAPLaunchHelper;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;

import java.util.ArrayList;

/**
 * We go via Hybris interface.
 * Initialize Hybris related queries, logic in this.
 */
public class HybrisHandler implements IAPExposedAPI {

    private int mThemeIndex;
    private Context mContext;
    private String mLanguage;
    private String mCountry;
    private IAPSettings mIAPSettings;

    public HybrisHandler(Context context, IAPSettings settings) {
        mContext = context;
        mThemeIndex = settings.getThemeIndex();
        mLanguage = settings.getLanguage();
        mCountry = settings.getCountry();
        mIAPSettings = settings;
    }

    @Override
    public void launchIAP(final int landingView, final String ctnNumber, final IAPHandlerListener listener) {
        if (isStoreInitialized()) {
            checkLaunchOrBuy(landingView, ctnNumber, listener);
        } else {
            initIAP(landingView, ctnNumber, listener);
        }
    }

    @Override
    public void getProductCartCount(final IAPHandlerListener iapHandlerListener) {
        if (isStoreInitialized()) {
            getProductCount(iapHandlerListener);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            getProductCount(iapHandlerListener);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapHandlerListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

    @Override
    public void getCompleteProductList(final IAPHandlerListener iapHandlerListener) {
        if (isStoreInitialized()) {
            getArrayListOfProductes(iapHandlerListener);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            getArrayListOfProductes(iapHandlerListener);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapHandlerListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

    private void getArrayListOfProductes(final IAPHandlerListener iapHandlerListener) {
        ProductCatalogPresenter presenter = new ProductCatalogPresenter();
        presenter.getCompleteProductList(mContext, new IAPHandlerListener() {
            @Override
            public void onSuccess(final int count) {
                updateSuccessListener(count, iapHandlerListener);
            }

            @Override
            public void onFailure(int errorCode) {
                iapHandlerListener.onFailure(errorCode);
            }

            @Override
            public void onFetchOfProductList(ArrayList<String> productList) {
                updateSuccessListener(productList, iapHandlerListener);
            }
        });
    }

    private boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }

    void checkLaunchOrBuy(int screen, String ctnNumber, IAPHandlerListener listener) {
        if (screen == IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW) {
            launchIAPActivity(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW);
        } else if (screen == IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW && TextUtils.isEmpty(ctnNumber)) {
            launchIAPActivity(IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW);
        } else if (screen == IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW) {
            launchIAPActivity(IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW);
        } else {
            buyProduct(ctnNumber, listener);
        }
    }

    void initIAP(final int screen, final String ctnNumber, final IAPHandlerListener listener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        delegate.getStore().initStoreConfig(mLanguage, mCountry, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                checkLaunchOrBuy(screen, ctnNumber, listener);
                if (listener != null) {
                    listener.onSuccess(IAPConstant.IAP_SUCCESS);
                }
            }

            @Override
            public void onError(final Message msg) {
                if (listener != null) {
                    listener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    void launchIAPActivity(int screen) {
        if (mIAPSettings.isLaunchAsFragment()) {
            IAPLaunchHelper.launchIAPAsFragment(mIAPSettings, screen);
        } else {
            IAPLaunchHelper.launchIAPActivity(mContext, screen, mThemeIndex);
        }
    }

    private void getProductCount(final IAPHandlerListener iapHandlerListener) {
        ShoppingCartAPI presenter = new ShoppingCartPresenter();
        presenter.getProductCartCount(mContext, new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                updateSuccessListener(count, iapHandlerListener);
            }

            @Override
            public void onFailure(final Message msg) {
                updateErrorListener(msg, iapHandlerListener);
            }
        }, null);
    }

    private void buyProduct(final String ctnNumber, final IAPHandlerListener listener) {
        ShoppingCartAPI presenter = new ShoppingCartPresenter();
        presenter.buyProduct(mContext, ctnNumber, new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                launchIAPActivity(IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW);
            }

            @Override
            public void onFailure(final Message msg) {
                updateErrorListener(msg, listener);
            }
        }, null);
    }

    private void updateErrorListener(final Message msg, final IAPHandlerListener iapHandlerListener) {
        if (iapHandlerListener != null) {
            iapHandlerListener.onFailure(getIAPErrorCode(msg));
        }
    }

    private void updateSuccessListener(final int count, final IAPHandlerListener iapHandlerListener) {
        if (iapHandlerListener != null) {
            iapHandlerListener.onSuccess(count);
        }
    }

    private void updateSuccessListener(final ArrayList<String> list, final IAPHandlerListener iapHandlerListener) {
        if (iapHandlerListener != null) {
            iapHandlerListener.onFetchOfProductList(list);
        }
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }
}
