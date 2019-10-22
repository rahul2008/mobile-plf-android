package com.philips.cdp.di.mec.integration;


import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * MECLaunchInput is responsible for initializing the settings required for launching.
 * @since 1.0.0
 */
public class MECLaunchInput extends UappLaunchInput implements Serializable {
    int mLandingView;
    public MECFlowInput mMECFlowInput;
    private MECListener mecListener;
    private ArrayList<String> mIgnoreRetailers;
    private ArrayList<String> mFirstIgnoreRetailers = new ArrayList<>();
    private String voucherCode;
    private MECOrderFlowCompletion mMecOrderFlowCompletion;
    private int maxCartCount;
    private boolean isHybrisSupported = true;
    private MECBannerEnabler mecBannerEnabler;
    /**
     * MECLaunchInput setMECFlow method to set the flow of uApp with required inputs
     * @param pLandingView  pass int value from MECFlows enums
     * @param pMecFlowInput  pass object of MECFlowInput
     * @param voucherCode pass String value of voucher Code
     * @param pBlackListedRetailer  pass list of retailer which you want to ignore from Retailer list
     * @since 1.0.0
     */
    public void setMECFlow(int pLandingView, MECFlowInput pMecFlowInput,String voucherCode, ArrayList<String> pBlackListedRetailer) {
        mLandingView = pLandingView;
        mMECFlowInput = pMecFlowInput;
        this.voucherCode=voucherCode;
        mIgnoreRetailers = pBlackListedRetailer;
    }

    /**
     * MECLaunchInput setMECFlow method to set the flow of uApp with required inputs
     * @param pLandingView  pass int value from MECFlows enums
     * @param pMecFlowInput  pass object of MECFlowInput
     * @param voucherCode pass String value of voucher Code
     * @since 1.0.0
     */
    public void setMECFlow(int pLandingView, MECFlowInput pMecFlowInput,String voucherCode) {
        mLandingView = pLandingView;
        mMECFlowInput = pMecFlowInput;
        this.voucherCode=voucherCode;
        mIgnoreRetailers = new ArrayList<>();
    }

    /**
     * Returns ignored or blacklisted retailers
     * @return ignoredRetailerList  pass list of retailer which you want to ignore from Retailer list
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
     * Returns MECListener instance
     * @return mecListener  instance of MECListener
     * @since 1.0.0
     */
    public MECListener getMecListener() {
        if (mecListener == null) new RuntimeException("Set MECListener in your vertical app ");
        return mecListener;
    }

    /**
     * Sets MECListener instance
     * @param mecListener  instance of MECListener for callbacks to proposition
     * @since 1.0.0
     */
    public void setMecListener(MECListener mecListener) {
        this.mecListener = mecListener;
    }

    /**
     *
     * @return voucher code
     * @since 1805
     */
    public String getVoucher(){
        return voucherCode;
    }

    /**
     *
     * @return MECOrderFlowCompletion
     * @since 1902
     */
    public MECOrderFlowCompletion getMecOrderFlowCompletion() {
        return mMecOrderFlowCompletion;
    }

    /**
     *
     * @param mMecOrderFlowCompletion
     * @since 1902
     */
    public void setMecOrderFlowCompletion(MECOrderFlowCompletion mMecOrderFlowCompletion) {
        this.mMecOrderFlowCompletion = mMecOrderFlowCompletion;
    }

    /**
     *
     * @return maxCartCount
     * @since 1902
     */
    public int getMaxCartCount() {
        return maxCartCount;
    }

    /**
     *
     * @param maxCartCount
     * 0 - means you can add any number of products in the cart
     * Anything beside 0 , will restrict the app to add products more than that count .
     * @since 1902
     */
    public void setMaxCartCount(int maxCartCount) {
        this.maxCartCount = maxCartCount;
    }

    /**
     *
     * @return isHybrisSupported
     * @since 1902
     */
    public boolean isHybrisSupported() {
        return isHybrisSupported;
    }

    /**
     *
     * @param hybrisSupported
     * false - set false , if you want to go with retailer flow only .
     * @since 1902
     */
    public void setHybrisSupported(boolean hybrisSupported) {
        isHybrisSupported = hybrisSupported;
    }

    /**
     *
     * @return MECBannerEnabler
     * @since 1902
     */
    public MECBannerEnabler getMecBannerEnabler() {
        return mecBannerEnabler;
    }

    /**
     *
     * @param mecBannerEnabler
     * MECBannerEnabler - set this by implementing getBannerView() method to show banner in MEC component .
     * @since 1902
     */
    public void setMecBannerEnabler(MECBannerEnabler mecBannerEnabler) {
        this.mecBannerEnabler = mecBannerEnabler;
    }

    /**
     * This enum is used to set the landing view of MEC
     * @since 1.0.0
     */
    public interface MECFlows {
        /**
         * To launch product catalog Screen
         * @since 1.0.0
         */
        int MEC_PRODUCT_CATALOG_VIEW = 0;
        /**
         * To launch shopping cart Screen
         * @since 1.0.0
         */
        int MEC_SHOPPING_CART_VIEW = 1;
        /**
         * To launch purchase history Screen
         * @since 1.0.0
         */
        int MEC_PURCHASE_HISTORY_VIEW = 2;
        /**
         * To launch product detail Screen
         * @since 1.0.0
         */
        int MEC_PRODUCT_DETAIL_VIEW = 3;
        /**
         * To launch buy direct Screen
         * @since 1.0.0
         */
        int MEC_BUY_DIRECT_VIEW = 4;
    }
}
