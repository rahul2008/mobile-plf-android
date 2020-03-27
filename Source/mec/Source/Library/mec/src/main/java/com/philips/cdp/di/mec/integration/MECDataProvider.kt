
/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.mec.integration.serviceDiscovery.MECManager
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.pif.DataInterface.MEC.MECDataInterface
import com.philips.platform.pif.DataInterface.MEC.listeners.MECCartUpdateListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECFetchCartListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECHybrisAvailabilityListener


import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.uappframework.uappinput.UappDependencies
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MECDataProvider : MECDataInterface {

    /**
     * Cart product count update .
     *
     * @param mecCartUpdateListener the Cart product count update listener
     * @since 2002.1.0
     */
    override fun cartUpdate(mecCartUpdateListener: MECCartUpdateListener?) {
        MECDataHolder.INSTANCE.mecCartUpdateListener=mecCartUpdateListener
    }

    private var mMECSettings: MECSettings?=null
    private var mUappDependencies: UappDependencies? = null
    private var mUserDataInterface: UserDataInterface? = null
    val MEC_NOTATION = "mec"


    override fun fetchCartCount(mecFetchCartListener: MECFetchCartListener) {
        GlobalScope.launch {
            var  mecManager: MECManager = MECManager()
            mecManager.getProductCartCountWorker(mecFetchCartListener)
        }
    }

    override fun isHybrisAvailable(mECHybrisAvailabilityListener: MECHybrisAvailabilityListener) {
        GlobalScope.launch {
            var  mecManager: MECManager = MECManager()
            mecManager.ishybrisavailableWorker(mECHybrisAvailabilityListener)
        }
    }


}