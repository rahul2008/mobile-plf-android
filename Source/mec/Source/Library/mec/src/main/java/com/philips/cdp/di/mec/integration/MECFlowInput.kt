package com.philips.cdp.di.mec.integration

import java.io.Serializable
import java.util.ArrayList

/**
 * IAPFlowInput initializes the required ctn’s for IAP to set the flow of micro app.
 * @since 1.0.0
 */
class MECFlowInput : Serializable {

    /**
     * returns product CTN number
     * @return productCTN  return product CTN
     * @since 1.0.0
     */
    var  productCTN: String? = null
    /**
     * returns product array of CTN number
     * @return productCTNs  returns array of product CTNs
     * @since 1.0.0
     */
    var productCTNs: ArrayList<String>? = null


    /**
     * creates instance of IAPFlowInput from product CTN number
     * @param productCTN  pass one product CTN as string
     * @since 1.0.0
     */
    constructor(productCTN: String) {
        this.productCTN = productCTN

    }

    /**
     * creates instance of IAPFlowInput from list of CTN numbers
     * @param prductCTNs  pass bunch of product CTNs as an array of string
     * @since 1.0.0
     */
    constructor(prductCTNs: ArrayList<String>) {
        this.productCTNs = prductCTNs
    }

    constructor()

}
