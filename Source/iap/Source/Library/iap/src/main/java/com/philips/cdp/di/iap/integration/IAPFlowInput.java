package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

/**
 * IAPFlowInput initializes the required ctn’s for IAP to set the flow of micro app.
 * @since 1.0.0
 */
public class IAPFlowInput {
    private String productCTN;
    private ArrayList<String> productCTNs;


    /**
     * creates instance of IAPFlowInput from product CTN number
     * @param productCTN  pass one product CTN as string
     * @since 1.0.0
     */
    public IAPFlowInput(String productCTN){
        this.productCTN = productCTN;

    }

    /**
     * creates instance of IAPFlowInput from list of CTN numbers
     * @param prductCTNs  pass bunch of product CTNs as an array of string
     * @since 1.0.0
     */
    public IAPFlowInput(ArrayList<String> prductCTNs) {
        this.productCTNs = prductCTNs;
    }

    /**
     * returns product CTN number
     * @return productCTN  return product CTN
     * @since 1.0.0
     */
    public String getProductCTN() {
        return productCTN;
    }

    /**
     * returns product array of CTN number
     * @return productCTNs  returns array of product CTNs
     * @since 1.0.0
     */
    public ArrayList<String> getProductCTNs() {
        return productCTNs;
    }

}
