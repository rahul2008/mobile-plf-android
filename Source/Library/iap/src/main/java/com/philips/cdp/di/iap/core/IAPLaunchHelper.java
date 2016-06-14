/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.Fragments.PurchaseHistoryFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.tagging.Tagging;

public class IAPLaunchHelper {

    public static void launchIAPActivity(Context context, int screen, int themeIndex) {
        //Set component version key and value for InAppPurchase
        Tagging.setComponentVersionKey(IAPAnalyticsConstant.COMPONENT_VERSION);
        Tagging.setComponentVersionVersionValue("In app purchase " + BuildConfig.VERSION_NAME);

        Intent intent = new Intent(context, IAPActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Check flag to differentiate shopping cart / product catalog
//        if (screen != IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW) {
        intent.putExtra(IAPConstant.IAP_IS_SHOPPING_CART_VIEW_SELECTED, screen);
//        }

        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, themeIndex);
        context.startActivity(intent);
    }

    public static void launchIAPAsFragment(IAPSettings config, int screen) {
        BaseAnimationSupportFragment target = getFragmentFromScreenID(screen);
        addFragment(config, target);
    }

    private static BaseAnimationSupportFragment getFragmentFromScreenID(final int screen) {
        BaseAnimationSupportFragment fragment = new ProductCatalogFragment();
        if (screen == IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW) {
            fragment = new ShoppingCartFragment();
        } else if (screen == IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW) {
            fragment = new PurchaseHistoryFragment();
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
}
