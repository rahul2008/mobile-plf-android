package com.philips.cdp.di.mec.common.ecsService

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.common.EcsConfigViewModel
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder

class ECSConfigServiceRepository {

    fun configECS(ecsConfigViewModel: EcsConfigViewModel){
        val ecsServices = MecHolder.INSTANCE.eCSServices

        ecsServices.configureECS( object : ECSCallback<Boolean, Exception>{
            override fun onResponse(result: Boolean?) {
                ecsConfigViewModel.isHybris.value = result
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsConfigViewModel.mecError.value = mecError
            }

        })
    }


    fun configECSToGetConfig(ecsConfigViewModel: EcsConfigViewModel){

        val ecsServices = MecHolder.INSTANCE.eCSServices

        ecsServices.configureECSToGetConfiguration(object:ECSCallback<ECSConfig,Exception>{
            override fun onResponse(result: ECSConfig?) {
                ecsConfigViewModel.ecsConfig.value = result
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsConfigViewModel.mecError.value = mecError
            }

        })
    }
}