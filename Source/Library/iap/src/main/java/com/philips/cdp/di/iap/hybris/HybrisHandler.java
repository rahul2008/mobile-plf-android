/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPLauncher;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.localematch.PILLocaleManager;

import java.util.ArrayList;

/**
 * We go via Hybris interface.
 * Initialize Hybris related queries, logic in this.
 */
public class HybrisHandler extends IAPLauncher implements IAPExposedAPI {

    private int mThemeIndex;
    private Context mContext;
    private String mLanguage;
    private String mCountry;
    private IAPSettings mIAPSettings;
    private IAPLaunchInput mIapConfig;
    private final int CURRENT_PAGE = 0;
    //Hybris default page size is 20. We are using the same
    private final int PAGE_SIZE = 20;
    PILLocaleManager localeManager;

    public HybrisHandler(Context context, IAPSettings settings) {
        mContext = context;
        mThemeIndex = settings.getThemeIndex();
        mLanguage = settings.getLanguage();
        mCountry = settings.getCountry();
        mIAPSettings = settings;
    }

    public HybrisHandler(Context context, IAPLaunchInput pIapConfig) {
        mContext = context;
        mLanguage = pIapConfig.getLanguage();
        mCountry = pIapConfig.getCountry();
        mIapConfig = pIapConfig;
    }

//    @Override
//    public void launchIAP(final int landingView, final String ctnNumber, final IAPHandlerListener listener) {
//        if (isStoreInitialized()) {
//            checkLaunchOrBuy(landingView, ctnNumber, listener);
//        } else {
//            initIAP(landingView, ctnNumber, listener);
//        }
//    }

//
//    @Override
//    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uAppListener) {
//        if (isStoreInitialized()) {
//            // checkLaunchOrBuy(landingView, ctnNumber, listener);
//            launchIAP(uiLauncher, uAppListener);
//        } else {
//           // initIAP(landingView, ctnNumber, listener);
//            initIAP(uiLauncher, (IAPHandlerListener) uAppListener);
//        }
//    }
//
//    private void launchIAP(UiLauncher uiLauncher, UappListener uAppListener) {
//        if (uiLauncher instanceof ActivityLauncher) {
//            launchActivity(mContext, mIapConfig, null);
//        } else if (uiLauncher instanceof FragmentLauncher) {
//            launchFragment(mIapConfig, (FragmentLauncher) uiLauncher);
//        }
//    }

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
    public void getCompleteProductList(final IAPHandlerProductListListener iapHandlerListener) {
        final ProductCatalogAPI presenter = ControllerFactory.getInstance().getProductCatalogPresenter(mContext, null, null);
        if (isStoreInitialized()) {
            presenter.getCompleteProductList(mContext, iapHandlerListener, 0, 10);
        } else {
            HybrisDelegate.getInstance(mContext).getStore().
                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
                        @Override
                        public void onSuccess(final Message msg) {
                            presenter.getCompleteProductList(mContext, iapHandlerListener, 0, 10);
                        }

                        @Override
                        public void onError(final Message msg) {
                            iapHandlerListener.onFailure(getIAPErrorCode(msg));
                        }
                    });
        }
    }

//    @Override
//    public void launchCategorizedCatalog(final ArrayList<String> pProductCTNs) {
//        if (mIAPSettings.isLaunchAsFragment()) {
//            IAPLaunchHelper.launchIAPAsFragment(mIAPSettings, IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null, pProductCTNs);
//        } else {
//            IAPLaunchHelper.launchIAPActivity(mContext, IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, mThemeIndex, null, pProductCTNs);
//        }
//    }

//    @Override
//    public void getCatalogCountAndCallCatalog() {
//
//        final ProductCatalogAPI mPresenter = ControllerFactory.getInstance()
//                .getProductCatalogPresenter(mContext, null, null);
//
//        final IAPHandlerListener listener = new IAPHandlerListener() {
//            @Override
//            public void onSuccess(final int count) {
//                mPresenter.getProductCatalog(0, count, null);
//            }
//
//            @Override
//            public void onFailure(final int errorCode) {
//
//            }
//        };
//
//        if (isStoreInitialized()) {
//            mPresenter.getCatalogCount(listener);
//        } else {
//            HybrisDelegate.getInstance(mContext).getStore().
//                    initStoreConfig(mLanguage, mCountry, new RequestListener() {
//                        @Override
//                        public void onSuccess(final Message msg) {
//                            mPresenter.getCatalogCount(listener);
//                        }
//
//                        @Override
//                        public void onError(final Message msg) {
//
//                        }
//                    });
//        }
//    }

    @Override
    public void buyDirect(String ctn) {
        if (isStoreInitialized()) {
            // checkLaunchOrBuy(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, ctn, null);
        } else {
            //initIAP(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, ctn, null);
        }
    }


    private boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }

//    void checkLaunchOrBuy(int screen, String ctnNumber, IAPHandlerListener listener) {
//        if (screen == IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW) {
//            launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, ctnNumber);
//        } else if (screen == IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW && TextUtils.isEmpty(ctnNumber)) {
//            launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, ctnNumber);
//        } else if (screen == IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW) {
//            launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, ctnNumber);
//        } else if (screen == IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW) {
//            launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, ctnNumber);
//        } else if (screen == IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW) {
//            launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, ctnNumber);
//        } else {
//            buyProduct(ctnNumber, listener);
//        }
//    }

//    void initIAP(final UiLauncher uiLauncher,final IAPHandlerListener listener) {
//        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
//        delegate.getStore().initStoreConfig(mLanguage, mCountry, new RequestListener() {
//            @Override
//            public void onSuccess(final Message msg) {
//               // checkLaunchOrBuy(screen, ctnNumber, listener);
//                if(uiLauncher instanceof ActivityLauncher){
//                    launchActivity(mContext, mIapConfig, (ActivityLauncher) uiLauncher);
//                }else{
//                    launchFragment(mIapConfig, (FragmentLauncher) uiLauncher);
//                }
//
//                if (listener != null) {
//                    listener.onSuccess(IAPConstant.IAP_SUCCESS);
//                }
//                //getCatalogCountAndCallCatalog();
//            }
//
//            @Override
//            public void onError(final Message msg) {
//                if (listener != null) {
//                    listener.onFailure(getIAPErrorCode(msg));
//                }
//            }
//        });
//    }

//    void launchIAPActivity(int screen, String ctnNumber) {
//        if (mIAPSettings.isLaunchAsFragment()) {
//            IAPLaunchHelper.launchIAPAsFragment(mIAPSettings, screen, ctnNumber, null);
//        } else {
//            IAPLaunchHelper.launchIAPActivity(mContext, screen, mThemeIndex, ctnNumber, null);
//        }
//    }

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
        });
    }

    private void buyProduct(final String ctnNumber, final IAPHandlerListener listener) {
        ShoppingCartAPI presenter = new ShoppingCartPresenter();
        presenter.buyProduct(mContext, ctnNumber, new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                //launchIAPActivity(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, ctnNumber);
            }

            @Override
            public void onFailure(final Message msg) {
                updateErrorListener(msg, listener);
            }
        });
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

    private void updateSuccessListener(final ArrayList<String> list, final IAPHandlerProductListListener iapHandlerListener) {
        if (iapHandlerListener != null) {
            iapHandlerListener.onSuccess(list);
        }
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }
}
