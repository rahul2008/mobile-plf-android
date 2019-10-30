package com.philips.cdp.di.mec.activity.ecsService

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.screens.catalog.ECSServiceRepository

class ECSConfigService {

    fun configECS(ecsCallback: ECSCallback<Boolean, Exception>){
        val ecsServices = MecHolder.INSTANCE.eCSServices

        ecsServices.configureECS(ecsCallback)
    }
}