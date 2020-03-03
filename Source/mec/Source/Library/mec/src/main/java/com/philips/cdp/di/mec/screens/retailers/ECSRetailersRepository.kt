package com.philips.cdp.di.mec.screens.retailers

import com.philips.cdp.di.ecs.ECSServices

class ECSRetailersRepository(private val ecsServices: ECSServices,private val ecsRetailerViewModel: ECSRetailerViewModel) {

    var eCSRetailerListCallback = ECSRetailerListCallback(ecsRetailerViewModel)

    fun getRetailers(ctn: String) {
        ecsServices.fetchRetailers(ctn, eCSRetailerListCallback)
    }

}