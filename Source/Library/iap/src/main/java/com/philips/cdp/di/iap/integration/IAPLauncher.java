package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
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
public class IAPLauncher implements UappInterface, IAPExposedAPI {
    private IAPLaunchInput mLaunchInput;
    private Context mContext;
    private IAPExposedAPI mImplementationHandler;

    @Override
    public void init(Context context, UappDependencies uappDependencies) {
        mContext = context;

        //setLangAndCountry(uappDependencies.getLanguage(), uappDependencies.getCountry());
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {
        mLaunchInput = (IAPLaunchInput) uappLaunchInput;
        String lang = mLaunchInput.setLanguage("en");
        String country = mLaunchInput.setCountry("US");
        mImplementationHandler = getExposedAPIImplementor(mContext, mLaunchInput);
        initHybrisDelegate(mContext, mLaunchInput);
        initControllerFactory(mLaunchInput);
        setLangAndCountry(lang, country);
//        if (uiLauncher instanceof ActivityLauncher) {
//            launchActivity(mContext, mLaunchInput, (ActivityLauncher) uiLauncher);
//        } else if (uiLauncher instanceof FragmentLauncher) {
//            launchFragment(mLaunchInput,(FragmentLauncher) uiLauncher);
//        }
        if (isStoreInitialized()) {
            // checkLaunchOrBuy(landingView, ctnNumber, listener);
            launchIAP(uiLauncher, mLaunchInput, uappListener);
        } else {
            // initIAP(landingView, ctnNumber, listener);
            initIAP(uiLauncher, mLaunchInput, (IAPHandlerListener) uappListener);
        }
    }

    private void launchIAP(UiLauncher uiLauncher, IAPLaunchInput pLaunchInput, UappListener uAppListener) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(mContext, pLaunchInput, (ActivityLauncher) uiLauncher);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchFragment(pLaunchInput, (FragmentLauncher) uiLauncher);
        }
    }

    void initIAP(final UiLauncher uiLauncher, final IAPLaunchInput mLaunchInput, final IAPHandlerListener listener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        delegate.getStore().initStoreConfig(mLaunchInput.getLanguage(), mLaunchInput.getCountry(), new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                // checkLaunchOrBuy(screen, ctnNumber, listener);
                if (uiLauncher instanceof ActivityLauncher) {
                    launchActivity(mContext, mLaunchInput, (ActivityLauncher) uiLauncher);
                } else {
                    launchFragment(mLaunchInput, (FragmentLauncher) uiLauncher);
                }

                if (listener != null) {
                    listener.onSuccess(IAPConstant.IAP_SUCCESS);
                }
                //getCatalogCountAndCallCatalog();
            }

            @Override
            public void onError(final Message msg) {
                if (listener != null) {
                    listener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    protected void launchFragment(IAPLaunchInput iapLaunchInput, FragmentLauncher uiLauncher) {
        BaseAnimationSupportFragment target = getFragmentFromScreenID(iapLaunchInput.mLandingViews, iapLaunchInput.mProductCTNs);
        addFragment(target, uiLauncher);
    }

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
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, activityLauncher.getUiKitTheme());
        intent.putStringArrayListExtra(IAPConstant.PRODUCT_CTNS, pLaunchConfig.mProductCTNs);
        pContext.startActivity(intent);
    }

    protected void addFragment(BaseAnimationSupportFragment newFragment, FragmentLauncher fragmentLauncher) {

        newFragment.setActionBarListener(fragmentLauncher.getActionbarListener());
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

    private void setLangAndCountry(final String language, final String country) {
        HybrisDelegate.getInstance().getStore().setLangAndCountry(language, country);
    }

    private boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }

    @Override
    public void getProductCartCount(IAPHandlerListener iapHandlerListener) {
        mImplementationHandler.getProductCartCount(iapHandlerListener);
    }

    @Override
    public void getCompleteProductList(IAPHandlerProductListListener iapHandlerListener) {
        mImplementationHandler.getCompleteProductList(iapHandlerListener);
    }

    @Override
    public void buyDirect(String ctn) {
        mImplementationHandler.buyDirect(ctn);
    }
}
