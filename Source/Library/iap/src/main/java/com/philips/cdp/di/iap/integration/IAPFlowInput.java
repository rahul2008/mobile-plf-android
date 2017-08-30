package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

public class IAPFlowInput {
    private String productCTN;
    private ArrayList<String> productCTNs;
    private ArrayList<String> mBlackListedRetailers;

    public IAPFlowInput(String productCTN, ArrayList<String> pBlackListedRetailer) {
        this.productCTN = productCTN;
        this.mBlackListedRetailers = pBlackListedRetailer;
    }

    public IAPFlowInput(ArrayList<String> prductCTNs, ArrayList<String> pBlackListedRetailer) {
        this.productCTNs = prductCTNs;
        this.mBlackListedRetailers = pBlackListedRetailer;
    }

    public String getProductCTN() {
        return productCTN;
    }

    public ArrayList<String> getProductCTNs() {
        return productCTNs;
    }

    public ArrayList<String> getBlackListedRetailer() {
        return mBlackListedRetailers;
    }
}
