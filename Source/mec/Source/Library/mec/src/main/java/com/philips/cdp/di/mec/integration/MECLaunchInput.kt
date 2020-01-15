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
    var blackListedRetailerNames : List<String>? = null
    var flowConfigurator: MECFlowConfigurator ? =null

    var supportsHybris: Boolean = true
    var supportsRetailer : Boolean = true

}
