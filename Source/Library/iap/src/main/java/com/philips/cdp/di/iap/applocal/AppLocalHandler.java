/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPListener;

public class AppLocalHandler extends IAPInterface implements IAPExposedAPI {

    private Context mContext;
    private int mThemeIndex;
    private IAPSettings mIAPSettings;
    private IAPLaunchInput mIAPConfig;

    public AppLocalHandler(Context context, IAPSettings iapSettings) {
        mContext = context;
        mIAPSettings = iapSettings;
    }

    public AppLocalHandler(Context pContext, IAPLaunchInput pIapConfig) {
        mContext = pContext;
        mIAPConfig = pIapConfig;
    }


//    @Override
//    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {
//        if (uiLauncher instanceof ActivityLauncher) {
//            launchActivity(mContext, mIAPConfig, (ActivityLauncher) uiLauncher);
//        } else if (uiLauncher instanceof FragmentLauncher) {
//            launchFragment(mIAPConfig, (FragmentLauncher) uiLauncher);
//        }
//        super.launch(uiLauncher, uappLaunchInput, uappListener);
//    }
//
//      @Override
//       public void launchIAP(final int landingView, final String ctnNumber, final IAPListener listener) {
//        if (mIAPSettings.isLaunchAsFragment()) {
//            //IAPLaunchHelper.launchIAPAsFragment(mIAPSettings, landingView, ctnNumber, null);
//        } else {
//            IAPLaunchHelper.launchIAPActivity(mContext,
//                    landingView, mThemeIndex, ctnNumber, null);
//        }
//     }

    /**
     * App local store doesn't support cart feature. Always return success with 0 count.
     *
     * @param iapListener
     */
    @Override
    public void getProductCartCount(final IAPListener iapListener) {
        if (iapListener != null) {
            iapListener.onGetCartCount(0);
        }
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        //NOP
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
//        //NOP
//    }

//    @Override
//    public void buyDirect(String ctn) {
//        //NOP
//    }
}