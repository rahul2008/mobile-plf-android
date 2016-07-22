/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.Fragments.ProductDetailFragment;
import com.philips.cdp.di.iap.Fragments.PurchaseHistoryFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.ArrayList;

public class IAPLaunchHelper {

    public static void launchIAPActivity(Context context, int screen, int themeIndex, String ctnNumber, ArrayList<String> ctns) {
        //Set component version key and value for InAppPurchase
    /*    Tagging.setComponentVersionKey(IAPAnalyticsConstant.COMPONENT_VERSION);
        Tagging.setComponentVersionVersionValue("In app purchase " + BuildConfig.VERSION_NAME);*/
        Intent intent = new Intent(context, IAPActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IAPConstant.IAP_IS_SHOPPING_CART_VIEW_SELECTED, screen);
        if (ctnNumber != null) {
            intent.putExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, ctnNumber);
        }
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, themeIndex);
        intent.putStringArrayListExtra(IAPConstant.PRODUCT_CTNS, ctns);
        context.startActivity(intent);
    }

    public static void launchIAPAsFragment(IAPSettings config, int screen, String ctnNumber, ArrayList<String> ctns) {
        BaseAnimationSupportFragment target = getFragmentFromScreenID(screen, ctnNumber,ctns);
        addFragment(config, target);
    }

    private static BaseAnimationSupportFragment getFragmentFromScreenID(final int screen, final String ctnNumber, ArrayList<String> ctns) {
        BaseAnimationSupportFragment fragment;
        if (screen == IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW) {
            fragment = new ShoppingCartFragment();
        } else if (screen == IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW) {
            fragment = new PurchaseHistoryFragment();
        } else if (screen == IAPConstant.IAPLandingViews.IAP_PRODUCT_DETAIL_VIEW) {
            fragment = new ProductDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, ctnNumber);
            fragment.setArguments(bundle);
        }else{
            fragment = new ProductCatalogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IAPConstant.PRODUCT_CTNS, ctnNumber);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private static void addFragment(IAPSettings config,
                                    BaseAnimationSupportFragment newFragment) {
        String tag = newFragment.getClass().getSimpleName();
        FragmentTransaction transaction = config.getFragmentManager().beginTransaction();
        transaction.replace(config.getContainerID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + tag + ")");
    }

/*    public static void launchIAPAsFragment(final IAPSettings mIAPSettings, final ArrayList<String> pProductCTNs) {
        BaseAnimationSupportFragment fragment = new ProductCatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NetworkConstants.PRODUCT_CTNS, pProductCTNs);
        fragment.setArguments(bundle);
        addFragment(mIAPSettings, fragment);
    }*/

    /*public static void launchIAPCatalog(final Context mContext, final int mThemeIndex, final ArrayList<String> pProductCTNs) {
        Tagging.setComponentVersionKey(IAPAnalyticsConstant.COMPONENT_VERSION);
        Tagging.setComponentVersionVersionValue("In app purchase " + BuildConfig.VERSION_NAME);
        Intent intent = new Intent(mContext, IAPActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, mThemeIndex);
       *//* Bundle bundle = new Bundle();
        bundle.putStringArrayList(NetworkConstants.PRODUCT_CTNS, pProductCTNs);
        intent.putExtra("bundle",bundle);*//*
        intent.putStringArrayListExtra(NetworkConstants.PRODUCT_CTNS, pProductCTNs);
        mContext.startActivity(intent);
    }*/
}
