package com.philips.cdp.di.iap.integration;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

public class IAPLaunchInput extends UappLaunchInput {
    int mLandingView;
    public IAPFlowInput mIAPFlowInput;
    private IAPListener iapListener;
    private ArrayList<String> mIgnoreRetailers;
    private ArrayList<String> mFirstIgnoreRetailers = new ArrayList<>();

    public void setIAPFlow(int pLandingView, IAPFlowInput pIapFlowInput, ArrayList<String> pBlackListedRetailer) {
        mLandingView = pLandingView;
        mIAPFlowInput = pIapFlowInput;
        mIgnoreRetailers = pBlackListedRetailer;
    }

    public void setIAPFlow(int pLandingView, IAPFlowInput pIapFlowInput) {
        mLandingView = pLandingView;
        mIAPFlowInput = pIapFlowInput;
        mIgnoreRetailers = new ArrayList<>();
    }


    public ArrayList<String> getIgnoreRetailers() {
        if (mIgnoreRetailers == null || mIgnoreRetailers.size() == 0) return mIgnoreRetailers;
        for (String str : mIgnoreRetailers) {
            String[] first = str.split(" ");
            String firstNameOfRetailer = first[0];
            mFirstIgnoreRetailers.add(firstNameOfRetailer);
        }
        return mFirstIgnoreRetailers;
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
