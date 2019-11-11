package com.philips.cdp.di.mec.utils

import com.philips.cdp.di.mec.integration.MECListener
import com.philips.platform.uappframework.listener.ActionBarListener

enum class MECDataHolder {

    INSTANCE;

    lateinit var actionbarUpdateListener: ActionBarListener
    lateinit var mecListener: MECListener

    fun setActionBarListener(mActionbarUpdateListener: ActionBarListener,mECListener: MECListener){
        actionbarUpdateListener = mActionbarUpdateListener
        mecListener= mECListener
    }

}