package com.philips.cdp.di.mec.integration

import java.io.Serializable
import java.util.ArrayList

/**
 * MECFlowConfigurator initializes the required ctn’s for MEC to set the flow of micro app.
 * @since 1.0.0
 */
class MECFlowConfigurator : Serializable {

    /**
     * returns product array of CTN number
     * @return productCTNs  returns array of product CTNs
     * @since 1.0.0
     */
    var productCTNs: ArrayList<String>? =null

    var landingView: MECLandingView? =null


    fun setCTNs(ctns:ArrayList<String>){
        productCTNs =ctns
    }


    enum class MECLandingView{
        MEC_PRODUCT_LIST_VIEW,
        MEC_PRODUCT_DETAILS_VIEW,
        MEC_CATEGORIZED_PRODUCT_LIST_VIEW
    }

}
