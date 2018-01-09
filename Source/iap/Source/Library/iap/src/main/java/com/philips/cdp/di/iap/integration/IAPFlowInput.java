package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

/**
 * It is used to accept CTNs from propositions
 */
public class IAPFlowInput {
    private String productCTN;
    private ArrayList<String> productCTNs;


    /**
     * creates instance of IAPFlowInput from product CTN number
     * @param productCTN - String productCTN
     * @since 1.0.0
     */
    public IAPFlowInput(String productCTN){
        this.productCTN = productCTN;

    }

    /**
     * creates instance of IAPFlowInput from product  CTN numbers
     * @param prductCTNs - ArrayList<String> prductCTNs
     * @since 1.0.0
     */
    public IAPFlowInput(ArrayList<String> prductCTNs) {
        this.productCTNs = prductCTNs;
    }

    /**
     *
     * @return productCTN - String product CTN number
     * @since 1.0.0
     */
    public String getProductCTN() {
        return productCTN;
    }

    /**
     * returns product CTN numbers
     * @return productCTNs - ArrayList<String> productCTNs
     * @since 1.0.0
     */
    public ArrayList<String> getProductCTNs() {
        return productCTNs;
    }

}
