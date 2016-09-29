package com.philips.cdp.di.iap.integration;


import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class IAPLaunchInput extends UappLaunchInput {
    int mLandingView;
    public IAPFlowInput mIAPFlowInput;
    private IAPListener iapListener;

    public void setIAPFlow(int pLandingView, IAPFlowInput pIapFlowInput) {
        mLandingView = pLandingView;
        mIAPFlowInput = pIapFlowInput;
    }

    public IAPListener getIapListener() {
        if (iapListener == null) new RuntimeException("Set IAPListener in your vertical app ");
        return iapListener;
    }

    public void setIapListener(IAPListener iapListener) {
        this.iapListener = iapListener;
    }

    public interface IAPFlows {
        int IAP_PRODUCT_CATALOG_VIEW = 0;
        int IAP_SHOPPING_CART_VIEW = 1;
        int IAP_PURCHASE_HISTORY_VIEW = 2;
        int IAP_PRODUCT_DETAIL_VIEW = 3;
        int IAP_BUY_DIRECT_VIEW = 4;
    }
}
