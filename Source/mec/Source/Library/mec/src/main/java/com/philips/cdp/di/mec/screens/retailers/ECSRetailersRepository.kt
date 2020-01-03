package com.philips.cdp.di.mec.screens.retailers

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder

class ECSRetailersRepository(private val ecsServices: ECSServices,private val ecsRetailerViewModel: ECSRetailerViewModel) {

    var eCSRetailerListCallback = ECSRetailerListCallback(ecsRetailerViewModel)

    fun getRetailers(ctn: String) {
        ecsServices.fetchRetailers(ctn, eCSRetailerListCallback)
    }

}