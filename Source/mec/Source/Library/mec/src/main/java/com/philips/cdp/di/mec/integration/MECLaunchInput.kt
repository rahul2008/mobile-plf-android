package com.philips.cdp.di.mec.integration


import com.philips.platform.uappframework.uappinput.UappLaunchInput

import java.io.Serializable
import java.util.ArrayList

/**
 * MECLaunchInput is responsible for initializing the settings required for launching.
 * @since 1.0.0
 */
class MECLaunchInput : UappLaunchInput(), Serializable {


    lateinit var mecBazaarVoiceInput: MECBazaarVoiceInput
    lateinit var mecBannerEnabler: MECBannerEnabler
    var mecListener: MECListener ? =null
    var mMECFlowInput: MECFlowInput ? =null
    var mLandingView: MECFlows? =null

    var hybrisEnabled: Boolean = true
    var retailerEnabled : Boolean = true

     enum class MECFlows{
        MEC_PRODUCT_CATALOG_VIEW,
        MEC_SHOPPING_CART_VIEW,
        MEC_PURCHASE_HISTORY_VIEW,
        MEC_PRODUCT_DETAIL_VIEW,
        MEC_BUY_DIRECT_VIEW,
        MEC_CATEGORIZED_VIEW
    }
}
