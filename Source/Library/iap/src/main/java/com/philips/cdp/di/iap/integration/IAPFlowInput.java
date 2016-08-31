package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

/**
 * Created by 310164421 on 8/22/2016.
 */
public class IAPFlowInput {
    private String productCTN;
    private ArrayList<String> productCTNs;

    public IAPFlowInput(String productCTN) {
        this.productCTN = productCTN;
    }

    public IAPFlowInput(ArrayList<String> prductCTNs) {
        this.productCTNs = prductCTNs;
    }

    public String getProductCTN() {
        return productCTN;
    }

    public ArrayList<String> getProductCTNs() {
        return productCTNs;
    }

}
