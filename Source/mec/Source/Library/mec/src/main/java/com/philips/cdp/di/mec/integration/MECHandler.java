/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.mec.activity.MECActivity;
import com.philips.cdp.di.mec.analytics.MECAnalytics;
import com.philips.cdp.di.mec.controller.ControllerFactory;
import com.philips.cdp.di.mec.mecHandler.HybrisHandler;
import com.philips.cdp.di.mec.mecHandler.LocalHandler;
import com.philips.cdp.di.mec.mecHandler.MECExposedAPI;
import com.philips.cdp.di.mec.networkEssentials.NetworkEssentials;
import com.philips.cdp.di.mec.networkEssentials.NetworkEssentialsFactory;
import com.philips.cdp.di.mec.screens.InAppBaseFragment;
import com.philips.cdp.di.mec.screens.MECProductCatalogFragment;
import com.philips.cdp.di.mec.session.HybrisDelegate;
import com.philips.cdp.di.mec.session.MECNetworkError;
import com.philips.cdp.di.mec.session.RequestListener;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;

class MECHandler {
    private MECDependencies mMECDependencies;
    private MECSettings mMECSetting;

    MECHandler(MECDependencies pMECDependencies,MECSettings pMecSettings) {
        mMECDependencies = pMECDependencies;
        mMECSetting = pMecSettings;
    }

    void initPreRequisite() {
        MECAnalytics.initIAPAnalytics(mMECDependencies);
    }

    void initIAPRequisite() {
        initHybrisDelegate();
    }

    void initControllerFactory() {
        ControllerFactory.getInstance().init(mMECSetting.isUseLocalData());
    }

    void initHybrisDelegate() {
        NetworkEssentials essentials = NetworkEssentialsFactory.getNetworkEssentials(mMECSetting.isUseLocalData());
        HybrisDelegate.getDelegateWithNetworkEssentials(essentials, mMECSetting,mMECDependencies);
    }

    void initIAP(final UiLauncher uiLauncher, final MECLaunchInput pLaunchInput) {
        final MECListener iapListener = pLaunchInput.getMecListener();
        HybrisDelegate delegate = HybrisDelegate.getInstance(mMECSetting.getContext());
        delegate.getStore().initStoreConfig(new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                onSuccessOfInitialization(uiLauncher, pLaunchInput, iapListener);
            }

            @Override
            public void onError(final Message msg) {
                onFailureOfInitialization(msg, iapListener);
            }
        });
    }


    void initMEC(final UiLauncher uiLauncher, final MECLaunchInput pLaunchInput) {
        final MECListener mecListener = pLaunchInput.getMecListener();
        onSuccessOfInitialization(uiLauncher, pLaunchInput, mecListener);
    }

    protected void onSuccessOfInitialization(UiLauncher uiLauncher, MECLaunchInput pLaunchInput, MECListener mecListener) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(mMECSetting.getContext(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else {
            launchAsFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }

        if (mecListener != null) {
            mecListener.onSuccess();
        }
    }

    protected void onFailureOfInitialization(Message msg, MECListener iapListener) {
        if (iapListener != null) {
            iapListener.onFailure(getIAPErrorCode(msg));
        }
    }


    void launchMEC(UiLauncher uiLauncher, MECLaunchInput pLaunchInput) {
        if(!verifyInput(pLaunchInput, pLaunchInput.mMECFlowInput))return;
        Utility.setVoucherCode(pLaunchInput.getVoucher());
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(mMECSetting.getContext(), pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }

    protected boolean verifyInput(MECLaunchInput launchInput, MECFlowInput input) {

        int landingScreen = launchInput.mLandingView;
        if (landingScreen == MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW
                || landingScreen == MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW) {
            if (input.getProductCTN() == null
                    || input.getProductCTN().equalsIgnoreCase("")) {

                launchInput.getMecListener().onFailure(MECConstant.MEC_ERROR_INVALID_CTN);
                return false;
            }
        } else if (landingScreen == MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW
                && (input == null || input.getProductCTNs() == null ||
                (input.getProductCTNs() != null && input.getProductCTNs().size() == 0))) {
            launchInput.getMecListener().onFailure(MECConstant.MEC_ERROR_INVALID_CTN);
            return false;
        }
        return true;
    }

    protected void launchAsActivity(Context pContext, MECLaunchInput pLaunchInput,
                                    ActivityLauncher activityLauncher) {
        Intent intent = new Intent(pContext, MECActivity.class);
        intent.putExtra(MECConstant.MEC_LANDING_SCREEN, pLaunchInput.mLandingView);

        if (pLaunchInput.mMECFlowInput != null) {
            if (pLaunchInput.mMECFlowInput.getProductCTN() != null) {
                intent.putExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                        pLaunchInput.mMECFlowInput.getProductCTN());
            }
            if (pLaunchInput.mMECFlowInput.getProductCTNs() != null) {
                intent.putStringArrayListExtra(MECConstant.CATEGORISED_PRODUCT_CTNS,
                        pLaunchInput.mMECFlowInput.getProductCTNs());
            }
            intent.putExtra(MECConstant.MEC_IGNORE_RETAILER_LIST, pLaunchInput.getIgnoreRetailers());
        }
        if(pLaunchInput.getVoucher()!=null) {
            Utility.setVoucherCode(pLaunchInput.getVoucher());
        }
        intent.putExtra(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        pContext.startActivity(intent);
    }

    protected void launchAsFragment(MECLaunchInput mecLaunchInput, FragmentLauncher uiLauncher) {
        InAppBaseFragment target = getFragment(mecLaunchInput.mLandingView, mecLaunchInput);
        addFragment(target, uiLauncher, mecLaunchInput.getMecListener());
    }

    protected InAppBaseFragment getFragment(final int screen, final MECLaunchInput mecLaunchInput) {
        InAppBaseFragment fragment = null;
        Bundle bundle = new Bundle();
        final ArrayList<String> ignoreRetailers = mecLaunchInput.getIgnoreRetailers();
        final String voucherCode=mecLaunchInput.getVoucher();
        switch (screen) {
            case MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW:
                //fragment = new ShoppingCartFragment();
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);
                Utility.setVoucherCode(mecLaunchInput.getVoucher());
                break;
            case MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW:
                //fragment = new PurchaseHistoryFragment();
                break;
            case MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW:
                /*fragment = new ProductDetailFragment();
                if (mecLaunchInput.mMECFlowInput!= null  && mecLaunchInput.mMECFlowInput.getProductCTN() != null) {
                    bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, mecLaunchInput.mMECFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);*/
                break;
            case MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW:
                /*fragment = new BuyDirectFragment();
                if (mecLaunchInput.mMECFlowInput!= null  && mecLaunchInput.mMECFlowInput.getProductCTN() != null) {
                    bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, mecLaunchInput.mMECFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);*/
                break;
            case MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW:
                fragment = new MECProductCatalogFragment();
                if (mecLaunchInput.mMECFlowInput!= null && mecLaunchInput.mMECFlowInput.getProductCTNs()!= null) {
                    ArrayList<String> CTNs = mecLaunchInput.mMECFlowInput.getProductCTNs();
                    bundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, CTNs);
                }

                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new MECProductCatalogFragment();
                bundle.putString(MECConstant.CATEGORISED_PRODUCT_CTNS, null);
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    protected void addFragment(InAppBaseFragment newFragment, FragmentLauncher fragmentLauncher, MECListener mecListener) {
        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), mecListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    protected MECExposedAPI getExposedAPIImplementor() {
        MECExposedAPI api;
        if (mMECSetting.isUseLocalData()) {
            api = new LocalHandler();
        } else {
            api = new HybrisHandler(mMECSetting.getContext());
        }
        return api;
    }

    protected boolean isStoreInitialized(Context pContext) {
        return HybrisDelegate.getInstance(pContext).getStore().isStoreInitialized();
    }

    protected int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof MECNetworkError) {
            return ((MECNetworkError) msg.obj).getIAPErrorCode();
        }
        return MECConstant.MEC_ERROR_UNKNOWN;
    }
}
