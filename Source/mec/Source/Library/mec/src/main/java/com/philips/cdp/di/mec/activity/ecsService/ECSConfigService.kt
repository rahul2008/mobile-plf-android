package com.philips.cdp.di.mec.activity.ecsService

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.mec.screens.catalog.ECSServiceRepository

class ECSConfigService {

    fun configECS(){
        val ecsServices = ECSServices("Tuscany2016", ECSServiceRepository.INSTANCE.appInfra)

        ecsServices.configureECS(object : ECSCallback<Boolean, Exception> {
            override fun onFailure(error: Exception?, ecsError: ECSError?) {

            }

            override fun onResponse(result: Boolean?) {

            }

        })
    }
}