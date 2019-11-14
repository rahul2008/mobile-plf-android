package com.philips.cdp.di.mec.utils

import com.philips.cdp.di.mec.integration.MECBannerEnabler
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.platform.uappframework.listener.ActionBarListener

enum class MECDataHolder {

    INSTANCE;

    lateinit var actionbarUpdateListener: ActionBarListener
    lateinit var mecListener: MECListener
    lateinit var mecBannerEnabler: MECBannerEnabler

    lateinit var bvClient: BVConversationsClient

    fun setActionBarListener(mActionbarUpdateListener: ActionBarListener,mECListener: MECListener){
        actionbarUpdateListener = mActionbarUpdateListener
        mecListener= mECListener
    }

}