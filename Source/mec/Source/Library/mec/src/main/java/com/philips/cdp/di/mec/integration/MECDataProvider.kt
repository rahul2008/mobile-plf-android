
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MECDataProvider : MECDataInterface {


    /**
     * Cart product count update .
     *
     * @param mecCartUpdateListener the Cart product count update listener
     * @since 2002.0
     */
    override fun addCartUpdateListener(mecCartUpdateListener: MECCartUpdateListener?) {
        MECDataHolder.INSTANCE.mecCartUpdateListener=mecCartUpdateListener
    }


    /**
     * This API is called to remove a previously added MECCartUpdateListener
     *
     * @param mecCartUpdateListener The listener to be removed
     * @since 2002.0
     */
    override fun removeCartUpdateListener(mecCartUpdateListener: MECCartUpdateListener?) {
       //  TODO("not implemented") 
    }
    


    override fun fetchCartCount(mECFetchCartListener: MECFetchCartListener) {
        GlobalScope.launch {
            var  mecManager: MECManager = MECManager()
            mecManager.getProductCartCountWorker(mECFetchCartListener)
        }
    }

    override fun isHybrisAvailable(mECHybrisAvailabilityListener: MECHybrisAvailabilityListener) {
        GlobalScope.launch {
            var  mecManager: MECManager = MECManager()
            mecManager.ishybrisavailableWorker(mECHybrisAvailabilityListener)
        }
    }


}