package com.philips.cdp.di.mec.integration


import com.philips.platform.uappframework.uappinput.UappLaunchInput

import java.io.Serializable
import java.util.ArrayList

/**
 * MECLaunchInput is responsible for initializing the settings required for launching.
 * @since 1.0.0
 */
class MECLaunchInput : UappLaunchInput(), Serializable {


    var mLandingView: Int = 0
    var mMECFlowInput: MECFlowInput? = null

    lateinit var mecBannerEnabler: MECBannerEnabler

    lateinit var mecBazaarVoiceInput: MECBazaarVoiceInput


    private var mIgnoreRetailers: ArrayList<String>? = null
    private val mFirstIgnoreRetailers = ArrayList<String>()
    private var voucherCode: String? = null


    var mecListener: MECListener? = null
        get() {
            if (field == null) RuntimeException("Set IAPListener in your vertical app ")
            return field
        }


    val ignoreRetailers: ArrayList<String>?
        get() {
            if (mIgnoreRetailers == null || mIgnoreRetailers!!.size == 0) return mIgnoreRetailers
            for (str in mIgnoreRetailers!!) {
                val first = str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val firstNameOfRetailer = first[0]
                mFirstIgnoreRetailers.add(firstNameOfRetailer)
            }
            return mFirstIgnoreRetailers
        }


    fun setMECFlow(pLandingView: Int, pMecFlowInput: MECFlowInput?, voucherCode: String?, pBlackListedRetailer: ArrayList<String>) {
        mLandingView = pLandingView
        mMECFlowInput = pMecFlowInput
        this.voucherCode = voucherCode
        mIgnoreRetailers = pBlackListedRetailer
    }


    fun setMECFlow(pLandingView: Int, pMecFlowInput: MECFlowInput?, voucherCode: String?) {
        mLandingView = pLandingView
        mMECFlowInput = pMecFlowInput
        this.voucherCode = voucherCode
        mIgnoreRetailers = ArrayList()
    }


    interface MECFlows {
        companion object {
            val MEC_PRODUCT_CATALOG_VIEW = 0
            val MEC_SHOPPING_CART_VIEW = 1
            val MEC_PURCHASE_HISTORY_VIEW = 2
            val MEC_PRODUCT_DETAIL_VIEW = 3
            val MEC_BUY_DIRECT_VIEW = 4
        }
    }
}
