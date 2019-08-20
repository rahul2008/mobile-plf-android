/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


import com.ecs.demouapp.ui.activity.ECSActivity;
import com.ecs.demouapp.ui.iapHandler.HybrisHandler;
import com.ecs.demouapp.ui.iapHandler.ECSExposedAPI;
import com.ecs.demouapp.ui.iapHandler.LocalHandler;
import com.ecs.demouapp.ui.screens.BuyDirectFragment;
import com.ecs.demouapp.ui.screens.InAppBaseFragment;
import com.ecs.demouapp.ui.screens.ProductCatalogFragment;
import com.ecs.demouapp.ui.screens.ProductDetailFragment;
import com.ecs.demouapp.ui.screens.PurchaseHistoryFragment;
import com.ecs.demouapp.ui.screens.ShoppingCartFragment;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.ecs.demouapp.ui.utils.Utility;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;

class ECSHandler {
    private ECSSettings mIAPSetting;

    ECSHandler(ECSSettings pIapSettings) {
        mIAPSetting = pIapSettings;
    }


    void initIAP(final UiLauncher uiLauncher, final ECSLaunchInput pLaunchInput) {
        final ECSListener iapListener = pLaunchInput.getIapListener();
        onSuccessOfInitialization(uiLauncher, pLaunchInput, iapListener);
    }

    protected void onSuccessOfInitialization(UiLauncher uiLauncher, ECSLaunchInput pLaunchInput, ECSListener iapListener) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(mIAPSetting.getContext(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else {
            launchAsFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }

        if (iapListener != null) {
            iapListener.onSuccess();
        }
    }


    void launchIAP(UiLauncher uiLauncher, ECSLaunchInput pLaunchInput) {
        if(!verifyInput(pLaunchInput, pLaunchInput.mIAPFlowInput))return;
        Utility.setVoucherCode(pLaunchInput.getVoucher());
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(mIAPSetting.getContext(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }

    protected boolean verifyInput(ECSLaunchInput launchInput, ECSFlowInput input) {

        int landingScreen = launchInput.mLandingView;
        if (landingScreen == ECSLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW
                || landingScreen == ECSLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW) {
            if (input.getProductCTN() == null
                    || input.getProductCTN().equalsIgnoreCase("")) {

                launchInput.getIapListener().onFailure(ECSConstant.IAP_ERROR_INVALID_CTN);
                return false;
            }
        } else if (landingScreen == ECSLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW
                && (input == null || input.getProductCTNs() == null ||
                (input.getProductCTNs() != null && input.getProductCTNs().size() == 0))) {
            launchInput.getIapListener().onFailure(ECSConstant.IAP_ERROR_INVALID_CTN);
            return false;
        }
        return true;
    }

    protected void launchAsActivity(Context pContext, ECSLaunchInput pLaunchInput,
                                    ActivityLauncher activityLauncher) {
        Intent intent = new Intent(pContext, ECSActivity.class);
        intent.putExtra(ECSConstant.IAP_LANDING_SCREEN, pLaunchInput.mLandingView);

        if (pLaunchInput.mIAPFlowInput != null) {
            if (pLaunchInput.mIAPFlowInput.getProductCTN() != null) {
                intent.putExtra(ECSConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                        pLaunchInput.mIAPFlowInput.getProductCTN());
            }
            if (pLaunchInput.mIAPFlowInput.getProductCTNs() != null) {
                intent.putStringArrayListExtra(ECSConstant.CATEGORISED_PRODUCT_CTNS,
                        pLaunchInput.mIAPFlowInput.getProductCTNs());
            }
            intent.putExtra(ECSConstant.IAP_IGNORE_RETAILER_LIST, pLaunchInput.getIgnoreRetailers());
        }
        if(pLaunchInput.getVoucher()!=null) {
            Utility.setVoucherCode(pLaunchInput.getVoucher());
        }
        intent.putExtra(ECSConstant.IAP_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        pContext.startActivity(intent);
    }

    protected void launchAsFragment(ECSLaunchInput iapLaunchInput, FragmentLauncher uiLauncher) {
        InAppBaseFragment target = getFragment(iapLaunchInput.mLandingView, iapLaunchInput);
        addFragment(target, uiLauncher, iapLaunchInput.getIapListener());
    }

    protected InAppBaseFragment getFragment(final int screen, final ECSLaunchInput iapLaunchInput) {
        InAppBaseFragment fragment;
        Bundle bundle = new Bundle();
        final ArrayList<String> ignoreRetailers = iapLaunchInput.getIgnoreRetailers();
        final String voucherCode=iapLaunchInput.getVoucher();
        switch (screen) {
            case ECSLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW:
                fragment = new ShoppingCartFragment();
                bundle.putStringArrayList(ECSConstant.IAP_IGNORE_RETAILER_LIST, ignoreRetailers);
                Utility.setVoucherCode(iapLaunchInput.getVoucher());
                break;
            case ECSLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW:
                fragment = new PurchaseHistoryFragment();
                break;
            case ECSLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW:
                fragment = new ProductDetailFragment();
                if (iapLaunchInput.mIAPFlowInput!= null  && iapLaunchInput.mIAPFlowInput.getProductCTN() != null) {
                    bundle.putString(ECSConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, iapLaunchInput.mIAPFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(ECSConstant.IAP_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);
                break;
            case ECSLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW:
                fragment = new BuyDirectFragment();
                if (iapLaunchInput.mIAPFlowInput!= null  && iapLaunchInput.mIAPFlowInput.getProductCTN() != null) {
                    bundle.putString(ECSConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, iapLaunchInput.mIAPFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(ECSConstant.IAP_IGNORE_RETAILER_LIST, ignoreRetailers);
                break;
            case ECSLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW:
                fragment = new ProductCatalogFragment();
                if (iapLaunchInput.mIAPFlowInput!= null && iapLaunchInput.mIAPFlowInput.getProductCTNs()!= null) {
                    ArrayList<String> CTNs = iapLaunchInput.mIAPFlowInput.getProductCTNs();
                    bundle.putStringArrayList(ECSConstant.CATEGORISED_PRODUCT_CTNS, CTNs);
                }

                bundle.putStringArrayList(ECSConstant.IAP_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new ProductCatalogFragment();
                bundle.putString(ECSConstant.CATEGORISED_PRODUCT_CTNS, null);
                bundle.putStringArrayList(ECSConstant.IAP_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    protected void addFragment(InAppBaseFragment newFragment, FragmentLauncher fragmentLauncher, ECSListener iapListener) {
        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), iapListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    protected ECSExposedAPI getExposedAPIImplementor() {
        ECSExposedAPI api;
        if (mIAPSetting.isUseLocalData()) {
            api = new LocalHandler();
        } else {
            api = new HybrisHandler();
        }
        return api;
    }
}
