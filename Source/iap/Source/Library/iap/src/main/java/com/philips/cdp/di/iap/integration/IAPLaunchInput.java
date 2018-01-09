package com.philips.cdp.di.iap.integration;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * It is used to get parameters from propositions for launching IAP
 */
public class IAPLaunchInput extends UappLaunchInput {
    int mLandingView;
    public IAPFlowInput mIAPFlowInput;
    private IAPListener iapListener;
    private ArrayList<String> mIgnoreRetailers;
    private ArrayList<String> mFirstIgnoreRetailers = new ArrayList<>();

    /**
     * It is used to set IAP flow with blacklisted retailers , launch as fragment or activity and landing view ID .
     * @param pLandingView - int pLandingView
     * @param pIapFlowInput - IAPFlowInput pIapFlowInput
     * @param pBlackListedRetailer - ArrayList<String> pBlackListedRetailer
     * @since 1.0.0
     */
    public void setIAPFlow(int pLandingView, IAPFlowInput pIapFlowInput, ArrayList<String> pBlackListedRetailer) {
        mLandingView = pLandingView;
        mIAPFlowInput = pIapFlowInput;
        mIgnoreRetailers = pBlackListedRetailer;
    }

    /**
     * It is used to set IAP flow to launch as fragment or activity and landing view ID .
     * @param pLandingView - int pLandingView
     * @param pIapFlowInput - IAPFlowInput pIapFlowInput
     * @since 1.0.0
     */
    public void setIAPFlow(int pLandingView, IAPFlowInput pIapFlowInput) {
        mLandingView = pLandingView;
        mIAPFlowInput = pIapFlowInput;
        mIgnoreRetailers = new ArrayList<>();
    }

    /**
     * returns ignored or blacklisted retailers
     * @return ignoredRetailerList - ArrayList<String> ignoreRetailers
     * @since 1.0.0
     */
    public ArrayList<String> getIgnoreRetailers() {
        if (mIgnoreRetailers == null || mIgnoreRetailers.size() == 0) return mIgnoreRetailers;
        for (String str : mIgnoreRetailers) {
            String[] first = str.split(" ");
            String firstNameOfRetailer = first[0];
            mFirstIgnoreRetailers.add(firstNameOfRetailer);
        }
        return mFirstIgnoreRetailers;
    }

    /**
     * returns IAPListener instance
     * @return iapListener - IAPListener iapListener
     * @since 1.0.0
     */
    public IAPListener getIapListener() {
        if (iapListener == null) new RuntimeException("Set IAPListener in your vertical app ");
        return iapListener;
    }

    /**
     * sets IAPListener instance
     * @param iapListener - IAPListener iapListener
     * @since 1.0.0
     */
    public void setIapListener(IAPListener iapListener) {
        this.iapListener = iapListener;
    }

    /**
     * interface to set landing view
     * @since 1.0.0
     */
    public interface IAPFlows {
        int IAP_PRODUCT_CATALOG_VIEW = 0;
        int IAP_SHOPPING_CART_VIEW = 1;
        int IAP_PURCHASE_HISTORY_VIEW = 2;
        int IAP_PRODUCT_DETAIL_VIEW = 3;
        int IAP_BUY_DIRECT_VIEW = 4;
    }
}
