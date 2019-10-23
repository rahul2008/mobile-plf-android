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

    public void setMECFlow(int pLandingView, MECFlowInput pMecFlowInput,String voucherCode, ArrayList<String> pBlackListedRetailer) {
        mLandingView = pLandingView;
        mMECFlowInput = pMecFlowInput;
        this.voucherCode=voucherCode;
        mIgnoreRetailers = pBlackListedRetailer;
    }


    public void setMECFlow(int pLandingView, MECFlowInput pMecFlowInput,String voucherCode) {
        mLandingView = pLandingView;
        mMECFlowInput = pMecFlowInput;
        this.voucherCode=voucherCode;
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


    public void setMecListener(MECListener mecListener) {
        this.mecListener = mecListener;
    }


    public interface MECFlows {
        int MEC_PRODUCT_CATALOG_VIEW = 0;
        int MEC_SHOPPING_CART_VIEW = 1;
        int MEC_PURCHASE_HISTORY_VIEW = 2;
        int MEC_PRODUCT_DETAIL_VIEW = 3;
        int MEC_BUY_DIRECT_VIEW = 4;
    }
}
