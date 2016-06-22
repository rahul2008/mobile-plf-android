/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.IAPLaunchHelper;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class AppLocalHandler implements IAPExposedAPI {

    private Context mContext;
    private int mThemeIndex;
    private IAPSettings mIAPSettings;

    public AppLocalHandler(Context context, IAPSettings config) {
        mContext = context;
        mThemeIndex = config.getThemeIndex();
        mIAPSettings = config;
    }

    @Override
    public void launchIAP(final int landingView, final String ctnNumber, final IAPHandlerListener listener) {
        if (mIAPSettings.isLaunchAsFragment()) {
            IAPLaunchHelper.launchIAPAsFragment(mIAPSettings, landingView);
        } else {
            IAPLaunchHelper.launchIAPActivity(mContext,
                    IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, mThemeIndex);
        }
    }

    /**
     * App local store doesn't support cart feature. Always return success with 0 count.
     *
     * @param iapHandlerListener
     */
    @Override
    public void getProductCartCount(final IAPHandlerListener iapHandlerListener) {
        if (iapHandlerListener != null) {
            iapHandlerListener.onSuccess(0);
        }
    }

    @Override
    public void getCompleteProductList(IAPHandlerProductListListener iapHandlerListener) {
        //NOP
    }

}