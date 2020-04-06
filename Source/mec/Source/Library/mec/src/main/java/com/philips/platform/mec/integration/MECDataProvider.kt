
/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.mec.integration


import com.android.volley.DefaultRetryPolicy
import com.philips.cdp.di.ecs.ECSServices
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.mec.R
import com.philips.platform.mec.integration.serviceDiscovery.MECManager
import com.philips.platform.mec.utils.MECDataHolder
import com.philips.platform.pif.DataInterface.MEC.MECDataInterface
import com.philips.platform.pif.DataInterface.MEC.MECException
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




    @Throws(MECException::class)
    override fun fetchCartCount(mECFetchCartListener: MECFetchCartListener)  {
        initECSSDK()
        if(MECDataHolder.INSTANCE.isInternetActive()) {
            if(MECDataHolder.INSTANCE.isUserLoggedIn()) {
                GlobalScope.launch {
                    var mecManager: MECManager = MECManager()
                    mecManager.getProductCartCountWorker(mECFetchCartListener)
                }
            }else{
                throw MECException(MECDataHolder.INSTANCE.appinfra.appInfraContext.getString(R.string.mec_cart_login_error_message),MECException.USER_NOT_LOGGED_IN)
            }
        }else{
            throw MECException(MECDataHolder.INSTANCE.appinfra.appInfraContext.getString(R.string.mec_no_internet),MECException.NO_INTERNET)
        }
    }

    @Throws(MECException::class)
    override fun isHybrisAvailable(mECHybrisAvailabilityListener: MECHybrisAvailabilityListener) {
        if(MECDataHolder.INSTANCE.isInternetActive()) {
            GlobalScope.launch {
                var mecManager: MECManager = MECManager()
                mecManager.ishybrisavailableWorker(mECHybrisAvailabilityListener)
            }
        }else{
            throw MECException(MECDataHolder.INSTANCE.appinfra.appInfraContext.getString(R.string.mec_no_internet),MECException.NO_INTERNET)
        }
    }

    private fun initECSSDK() {
        val configError = AppConfigurationInterface.AppConfigurationError()
        val propositionID = MECDataHolder.INSTANCE.appinfra.configInterface.getPropertyForKey("propositionid", "MEC", configError)
        var propertyForKey = ""
        if (propositionID != null) {
            propertyForKey = propositionID as String
        }

        var voucher: Boolean = true // if voucher key is not mentioned Appconfig then by default it will be considered True
        try {
            voucher =MECDataHolder.INSTANCE.appinfra.configInterface.getPropertyForKey("voucherCode.enable", "MEC", configError) as Boolean
        } catch (e: Exception) {

        }

        MECDataHolder.INSTANCE.propositionId = propertyForKey
        MECDataHolder.INSTANCE.voucherEnabled = voucher
        val ecsServices = ECSServices(propertyForKey, MECDataHolder.INSTANCE.appinfra as AppInfra)

        val defaultRetryPolicy = DefaultRetryPolicy( // 30 second time out
                30000,
                0,
                0f)
        ecsServices.setVolleyTimeoutAndRetryCount(defaultRetryPolicy)

        MECDataHolder.INSTANCE.eCSServices = ecsServices // singleton

    }


}