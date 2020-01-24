package com.philips.cdp.di.mec.utils

import com.philips.cdp.di.mec.integration.MECBannerConfigurator
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.philips.cdp.di.mec.integration.MECBazaarVoiceInput
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.platform.appinfra.AppInfraInterface
import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.uappframework.listener.ActionBarListener
import java.util.ArrayList

enum class MECDataHolder {

    INSTANCE;

    lateinit var appinfra: AppInfraInterface
    lateinit var actionbarUpdateListener: ActionBarListener
    lateinit var mecListener: MECListener
    lateinit var mecBannerEnabler: MECBannerConfigurator
    lateinit var locale:String
    lateinit var propositionId:String
    lateinit var userDataInterface: UserDataInterface
    lateinit var refreshToken:String
    var blackListedRetailers: List<String> ?=null
    lateinit var mecBazaarVoiceInput: MECBazaarVoiceInput
    private var privacyUrl: String? = null
    var hybrisEnabled: Boolean = true
    var retailerEnabled :Boolean = true
    var rootCategory:String = ""

    fun getPrivacyUrl(): String? {
        return privacyUrl
    }

    fun setPrivacyUrl(privacyUrl: String) {
        this.privacyUrl = privacyUrl
    }



    var bvClient: BVConversationsClient? = null

    fun setActionBarListener(mActionbarUpdateListener: ActionBarListener,mECListener: MECListener){
        actionbarUpdateListener = mActionbarUpdateListener
        mecListener= mECListener
    }

}