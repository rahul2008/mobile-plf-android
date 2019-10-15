/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.mec.activity.MECLauncherActivity;
import com.philips.cdp.di.mec.screens.InAppBaseFragment;
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

class MECHandler {
    private MECDependencies mMECDependencies;
    private MECSettings mMECSetting;
    private UiLauncher mUiLauncher;
    private MECLaunchInput mLaunchInput;

    MECHandler(MECDependencies pMECDependencies,MECSettings pMecSettings,UiLauncher pUiLauncher,  MECLaunchInput pLaunchInput) {
        this.mMECDependencies = pMECDependencies;
        this.mMECSetting = pMecSettings;
        this.mUiLauncher = pUiLauncher;
        this.mLaunchInput = pLaunchInput;

    }


    void launchMEC() {
        if (mUiLauncher instanceof ActivityLauncher) {
            launchMECasActivity();
        } else {
            launchMECasFragment();
        }
    }


    protected void launchMECasActivity() {
        Intent intent = new Intent(mMECSetting.getContext(), MECLauncherActivity.class);
        intent.putExtra(MECConstant.MEC_LANDING_SCREEN, mLaunchInput.mLandingView);
        ActivityLauncher activityLauncher =  (ActivityLauncher) mUiLauncher;
        Bundle mBundle = new Bundle();
        //mBundle.putSerializable("LaunchInput",(UappLaunchInput)mLaunchInput);
       // mBundle.putSerializable("UILauncher",mUiLauncher);
        mBundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        intent.putExtras(mBundle);
        mMECSetting.getContext().startActivity(intent);

      /*  if (pLaunchInput.mMECFlowInput != null) {
            if (pLaunchInput.mMECFlowInput.getProductCTN() != null) {
                intent.putExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                        pLaunchInput.mMECFlowInput.getProductCTN());
            }
            if (pLaunchInput.mMECFlowInput.getProductCTNs() != null) {
                intent.putStringArrayListExtra(MECConstant.CATEGORISED_PRODUCT_CTNS,
                        pLaunchInput.mMECFlowInput.getProductCTNs());
            }
            intent.putExtra(MECConstant.MEC_IGNORE_RETAILER_LIST, pLaunchInput.getIgnoreRetailers());
        }*/

    }

    protected void launchMECasFragment() {
        InAppBaseFragment target = getFragment(mLaunchInput.mLandingView, mLaunchInput);
        addFragment(target, (FragmentLauncher) mUiLauncher, mLaunchInput.getMecListener());
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
        //newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), mecListener);
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }


}
