
/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.mec.integration


import com.philips.platform.mec.integration.serviceDiscovery.MECManager
import com.philips.platform.mec.utils.MECDataHolder
import com.philips.platform.pif.DataInterface.MEC.MECDataInterface
import com.philips.platform.pif.DataInterface.MEC.listeners.MECCartUpdateListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECFetchCartListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECHybrisAvailabilityListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MECDataProvider : MECDataInterface {



    override fun addCartUpdateListener(mecCartUpdateListener: MECCartUpdateListener?) {
        MECDataHolder.INSTANCE.mecCartUpdateListener=mecCartUpdateListener
    }


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