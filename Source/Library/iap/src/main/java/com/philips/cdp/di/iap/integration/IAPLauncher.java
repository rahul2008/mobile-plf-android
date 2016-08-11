package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.BuyDirectFragment;
import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.Fragments.ProductDetailFragment;
import com.philips.cdp.di.iap.Fragments.PurchaseHistoryFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.applocal.AppLocalHandler;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.NetworkEssentialsFactory;
import com.philips.cdp.di.iap.hybris.HybrisHandler;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * Created by Apple on 10/08/16.
 */
public class IAPLauncher implements UappInterface {
    private IAPLaunchInput mLaunchInput;
    private Context mContext;

    @Override
    public void init(Context context, UappDependencies uappDependencies) {
        mContext = context;

    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {
        mLaunchInput = (IAPLaunchInput) uappLaunchInput;
        getExposedAPIImplementor(mContext, mLaunchInput);
        initHybrisDelegate(mContext, mLaunchInput);
        initControllerFactory(mLaunchInput);
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(mContext, mLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchFragment(mLaunchInput,(FragmentLauncher) uiLauncher);
        }
    }

    protected void launchFragment(IAPLaunchInput iapLaunchInput, FragmentLauncher uiLauncher) {
        BaseAnimationSupportFragment target = getFragmentFromScreenID(iapLaunchInput.mLandingViews, iapLaunchInput.mProductCTNs);
        addFragment(target, uiLauncher);
    }

//    @Override
//    public void setLaunchConfig(LaunchConfig launchConfig) {
//        mLaunchConfig = (IAPLaunchInput) launchConfig;
//    }

    private BaseAnimationSupportFragment getFragmentFromScreenID(final int screen, final ArrayList<String> ctnNumbers) {
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
                bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, ctnNumbers.get(0));
                fragment.setArguments(bundle);
                break;
            case IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW:
                fragment = new BuyDirectFragment();
                bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, ctnNumbers.get(0));
                break;
            default:
                //Default redirecting to IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW:
                fragment = new ProductCatalogFragment();
                bundle.putString(IAPConstant.PRODUCT_CTNS, null);
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    protected void launchActivity(Context pContext, IAPLaunchInput pLaunchConfig, ActivityLauncher activityLauncher) {
        Intent intent = new Intent(pContext, IAPActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IAPConstant.IAP_IS_SHOPPING_CART_VIEW_SELECTED, pLaunchConfig.mLandingViews);
        if (pLaunchConfig.mProductCTNs != null) {
            intent.putExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER, pLaunchConfig.mProductCTNs);
        }
        //TODO : Activity Theme has to get from ActivityLauncher
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, 1);
        intent.putStringArrayListExtra(IAPConstant.PRODUCT_CTNS, pLaunchConfig.mProductCTNs);
        pContext.startActivity(intent);
    }

    protected void addFragment(BaseAnimationSupportFragment newFragment, FragmentLauncher fragmentLauncher) {

        String tag = newFragment.getClass().getSimpleName();
        FragmentTransaction transaction = fragmentLauncher.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentLauncher.getParentContainerResourceID(), newFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + tag + ")");
    }

    private IAPExposedAPI getExposedAPIImplementor(Context context, IAPLaunchInput iapLaunchInput) {
        IAPExposedAPI api = null;
        if (iapLaunchInput.isUseLocalData()) {
            api = new AppLocalHandler(context, iapLaunchInput);
        } else {
            api = new HybrisHandler(context, iapLaunchInput);
        }
        return api;
    }

    private void initHybrisDelegate(Context context, IAPLaunchInput iapConfig) {
        int requestCode = getNetworkEssentialReqeustCode(iapConfig.isUseLocalData());
        NetworkEssentials essentials = NetworkEssentialsFactory.getNetworkEssentials(requestCode);
        HybrisDelegate.getDelegateWithNetworkEssentials(context, essentials);
    }


    private int getNetworkEssentialReqeustCode(boolean useLocalData) {
        int requestCode = NetworkEssentialsFactory.LOAD_HYBRIS_DATA;
        if (useLocalData) {
            requestCode = NetworkEssentialsFactory.LOAD_LOCAL_DATA;
        }

        return requestCode;
    }

    private void initControllerFactory(IAPLaunchInput iapConfig) {
        int requestCode = getNetworkEssentialReqeustCode(iapConfig.isUseLocalData());
        ControllerFactory.getInstance().init(requestCode);
    }


}
