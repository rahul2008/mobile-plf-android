package com.philips.cdp.di.iap.utils;

import android.view.View;

import com.philips.cdp.di.iap.integration.IAPOrderFlowCompletion;

/**
 * Created by philips on 5/2/19.
 */

public class IAPUtility {

    private int maxCartCount;
    private IAPOrderFlowCompletion iapOrderFlowCompletion;
    private boolean isHybrisSupported = true;
    private View bannerView;
    private boolean isVoucherEnable;

    private IAPUtility() {
    }

    public int getMaxCartCount() {
        return maxCartCount;
    }

    public void setMaxCartCount(int maxCartCount) {
        this.maxCartCount = maxCartCount;
    }

    public IAPOrderFlowCompletion getIapOrderFlowCompletion() {
        return iapOrderFlowCompletion;
    }

    public void setIapOrderFlowCompletion(IAPOrderFlowCompletion iapOrderFlowCompletion) {
        this.iapOrderFlowCompletion = iapOrderFlowCompletion;
    }

    public boolean isHybrisSupported() {
        return isHybrisSupported;
    }

    public void setHybrisSupported(boolean hybrisSupported) {
        isHybrisSupported = hybrisSupported;
    }

    public View getBannerView() {
        return bannerView;
    }

    public void setBannerView(View bannerView) {
        this.bannerView = bannerView;
    }

    public boolean isVoucherEnable() {
        return isVoucherEnable;
    }

    public void setVoucherEnable(boolean voucherEnable) {
        isVoucherEnable = voucherEnable;
    }

    private static class IAPUtilitySingleton
    {
        private static final IAPUtility INSTANCE = new IAPUtility();
    }

    public static IAPUtility getInstance()
    {
        return IAPUtilitySingleton.INSTANCE;
    }

}
