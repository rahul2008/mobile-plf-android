package com.philips.cdp.di.mec.integration


import com.philips.platform.uappframework.uappinput.UappLaunchInput

import java.io.Serializable

/**
 * MECLaunchInput is responsible for initializing the settings required for launching.
 * @since 1.0.0
 */
class MECLaunchInput : UappLaunchInput(), Serializable {


    lateinit var mecBazaarVoiceInput: MECBazaarVoiceInput
    lateinit var mecBannerConfigurator: MECBannerConfigurator

    //TODO :-Blacklist retailer
    var mecListener: MECListener ? =null
    var flowConfigurator: MECFlowConfigurator ? =null

    //TODO Remove this
    var mLandingView: MECLandingView? =null

    var supportsHybris: Boolean = true
    var supportsRetailer : Boolean = true

     enum class MECLandingView{
        MEC_PRODUCT_LIST_VIEW,
        MEC_PRODUCT_DETAILS_VIEW,
        MEC_CATEGORIZED_PRODUCT_LIST_VIEW
    }
}
