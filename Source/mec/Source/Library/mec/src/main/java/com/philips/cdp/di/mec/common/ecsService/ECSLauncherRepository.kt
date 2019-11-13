package com.philips.cdp.di.mec.common.ecsService

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import java.util.*

class ECSLauncherRepository {

    fun configECS(ecsLauncherViewModel: EcsLauncherViewModel){
        val ecsServices = MecHolder.INSTANCE.eCSServices

        ecsServices.configureECS( object : ECSCallback<Boolean, Exception>{
            override fun onResponse(result: Boolean?) {
                ecsLauncherViewModel.isHybris.value = result
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsLauncherViewModel.mecError.value = mecError
            }

        })
    }


    fun configECSToGetConfig(ecsLauncherViewModel: EcsLauncherViewModel){

        val ecsServices = MecHolder.INSTANCE.eCSServices

        ecsServices.configureECSToGetConfiguration(object:ECSCallback<ECSConfig,Exception>{
            override fun onResponse(result: ECSConfig?) {
                ecsLauncherViewModel.ecsConfig.value = result
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsLauncherViewModel.mecError.value = mecError
            }

        })
    }


    fun getProductDetailForCtn(ctn: String, ecsLauncherViewModel: EcsLauncherViewModel) {

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProduct(ctn,object:ECSCallback<ECSProduct,Exception>{
            override fun onResponse(result: ECSProduct) {
                ecsLauncherViewModel.ecsProduct.value = result
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsLauncherViewModel.mecError.value = mecError
            }
        })
    }

    fun getRetailerProductDetailForCtn(ctn: String, ecsLauncherViewModel: EcsLauncherViewModel) {

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProductSummaries(Arrays.asList(ctn),object:ECSCallback<List<ECSProduct>,Exception>{
            override fun onResponse(result: List<ECSProduct>) {
                ecsLauncherViewModel.ecsProduct.value = result.get(0)
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsLauncherViewModel.mecError.value = mecError
            }
        })
    }
}