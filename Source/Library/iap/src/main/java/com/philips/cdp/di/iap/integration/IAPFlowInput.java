package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

public class IAPFlowInput {
    private String productCTN;
    private ArrayList<String> productCTNs;


    public IAPFlowInput(String productCTN){
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
