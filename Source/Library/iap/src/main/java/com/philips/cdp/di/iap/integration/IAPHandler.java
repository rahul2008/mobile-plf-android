/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.BuyDirectFragment;
import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.Fragments.ProductDetailFragment;
import com.philips.cdp.di.iap.Fragments.PurchaseHistoryFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.applocal.AppLocalHandler;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.NetworkEssentialsFactory;
import com.philips.cdp.di.iap.hybris.HybrisHandler;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.io.Serializable;
import java.util.Locale;

class IAPHandler {
    private Context mContext;
    private IAPDependencies mIAPDependencies;
    private IAPListener iapListener;
    private IAPSettings mIAPSetting;

    IAPHandler(IAPDependencies mIAPDependencies, IAPSettings pIapSettings) {
        this.mIAPDependencies = mIAPDependencies;
        mIAPSetting = pIapSettings;
        mContext = mIAPSetting.getContext();
    }

    void initTaggingLogging() {
        IAPAnalytics.initIAPAnalytics(mIAPDependencies);
        IAPLog.initIAPLog(mIAPDependencies);
    }


    void initIAP() {
        initHybrisDelegate(mContext, mIAPSetting, mIAPDependencies);
        initControllerFactory(mIAPSetting);
        setLangAndCountry();
    }

    IAPExposedAPI getExposedAPIImplementor() {
        return getExposedAPIImplementor(mContext, mIAPSetting);
    }

    void launchIAP(UiLauncher uiLauncher, IAPLaunchInput pLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(mContext, pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }

    void initIAP(final UiLauncher uiLauncher, final IAPLaunchInput pLaunchInput) {
        iapListener = pLaunchInput.getIapListener();
        //User logged off scenario
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        delegate.getStore().initStoreConfig(CartModelContainer.getInstance().getLanguage(), CartModelContainer.getInstance().getCountry(), new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (uiLauncher instanceof ActivityLauncher) {
                    launchActivity(mContext, pLaunchInput, (ActivityLauncher) uiLauncher);
                } else {
                    launchFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
                }

                if (iapListener != null) {
                    iapListener.onSuccess();
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapListener != null) {
                    iapListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    private void launchFragment(IAPLaunchInput iapLaunchInput, FragmentLauncher uiLauncher) {
        BaseAnimationSupportFragment target = getFragmentFromScreenID(iapLaunchInput.mLandingView, iapLaunchInput.mIAPFlowInput);
        addFragment(target, uiLauncher);
    }

    private BaseAnimationSupportFragment getFragmentFromScreenID(final int screen, final IAPFlowInput iapFlowInput) {
        BaseAnimationSupportFragment fragment;
        Bundle bundle = new Bundle();
        switch (screen) {
            case IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW:
                fragment = new ShoppingCartFragment();
                break;
            case IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW:
                fragment = new PurchaseHistoryFragment();
                break;
            case IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW:
                fragment = new ProductDetailFragment();
                bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, iapFlowInput.getProductCTN());
                fragment.setArguments(bundle);
                break;
            case IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW:
                fragment = new BuyDirectFragment();
                bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, iapFlowInput.getProductCTN());
                break;
            default:
                //Default redirecting to IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW:
                fragment = new ProductCatalogFragment();
                bundle.putString(IAPConstant.CAEGORIZED_PRODUCT_CTNS, null);
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    private void launchActivity(Context pContext, IAPLaunchInput pLaunchConfig,
                                ActivityLauncher activityLauncher) {
        Intent intent = new Intent(pContext, IAPActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, pLaunchConfig.mLandingView);

        if (pLaunchConfig.mLandingView == IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW
                || pLaunchConfig.mLandingView
                == IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW) {
            if (pLaunchConfig.mIAPFlowInput.getProductCTN() == null
                    || pLaunchConfig.mIAPFlowInput.getProductCTN().equalsIgnoreCase("")) {
                throw new RuntimeException("Please Pass CTN");
            } else {
                intent.putExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER,
                        pLaunchConfig.mIAPFlowInput.getProductCTN());
            }
        }

        if (pLaunchConfig.mIAPFlowInput != null) {
            if (pLaunchConfig.mIAPFlowInput.getProductCTNs() != null)
                intent.putStringArrayListExtra(IAPConstant.CAEGORIZED_PRODUCT_CTNS,
                        pLaunchConfig.mIAPFlowInput.getProductCTNs());
        }

        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        pContext.startActivity(intent);
    }

    protected void addFragment(BaseAnimationSupportFragment newFragment, FragmentLauncher fragmentLauncher) {
        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), iapListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getName() + "   ("
                + tag + ")");
    }

    private IAPExposedAPI getExposedAPIImplementor(Context context, IAPSettings iapSettings) {
        IAPExposedAPI api;
        if (iapSettings.isUseLocalData()) {
            api = new AppLocalHandler(context);
        } else {
            api = new HybrisHandler(context);
        }
        return api;
    }

    private void initHybrisDelegate(Context context, IAPSettings iapSettings, IAPDependencies iapDependencies) {
        int requestCode = getNetworkEssentialReqeustCode(iapSettings.isUseLocalData());
        NetworkEssentials essentials = NetworkEssentialsFactory.getNetworkEssentials(requestCode);
        HybrisDelegate.getDelegateWithNetworkEssentials(context, essentials, iapDependencies);
    }


    private int getNetworkEssentialReqeustCode(boolean useLocalData) {
        int requestCode = NetworkEssentialsFactory.LOAD_HYBRIS_DATA;
        if (useLocalData) {
            requestCode = NetworkEssentialsFactory.LOAD_LOCAL_DATA;
        }

        return requestCode;
    }

    private void initControllerFactory(IAPSettings iapSettings) {
        int requestCode = getNetworkEssentialReqeustCode(iapSettings.isUseLocalData());
        ControllerFactory.getInstance().init(requestCode);
    }

    private void setLangAndCountry() {
        PILLocaleManager localeManager = new PILLocaleManager(mContext);
        String[] localeArray;
        String localeAsString = localeManager.getInputLocale();
        localeArray = localeAsString.split("_");
        Locale locale = new Locale(localeArray[0], localeArray[1]);
        CartModelContainer.getInstance().setLanguage(locale.getLanguage());
        CartModelContainer.getInstance().setCountry(locale.getCountry());
        HybrisDelegate.getInstance().getStore().setLangAndCountry(locale.getLanguage(), locale.getCountry());
    }

    boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }
}
