package com.philips.cdp.di.mec.integration

import java.io.Serializable
import java.util.ArrayList

/**
 * IAPFlowInput initializes the required ctn’s for IAP to set the flow of micro app.
 * @since 1.0.0
 */
class MECFlowConfigurator : Serializable {

    //TODO Remove productCTN
    /**
     * returns product CTN number
     * @return productCTN  return product CTN
     * @since 1.0.0
     */
    var  productCTN: String ?=null
    /**
     * returns product array of CTN number
     * @return productCTNs  returns array of product CTNs
     * @since 1.0.0
     */
    var productCTNs: ArrayList<String>? =null

    var landingView: MECLandingView? =null

    fun setCTN(ctn : String){
        productCTN = ctn
    }

    fun setCTNs(ctns:ArrayList<String>){
        productCTNs =ctns
    }




    enum class MECLandingView{
        MEC_PRODUCT_LIST_VIEW,
        MEC_PRODUCT_DETAILS_VIEW,
        MEC_CATEGORIZED_PRODUCT_LIST_VIEW
    }

}
