package com.philips.cdp.di.mec.common.ecsService

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import java.util.*

class ECSLauncherRepository(private val ecsServices: ECSServices, private val ecsLauncherViewModel: EcsLauncherViewModel) {

    var configBooleanCallback = ConfigBooleanCallback(ecsLauncherViewModel)

    var configurationCallback = ConfigurationCallback(ecsLauncherViewModel)

    var ecsProductCallback = ECSProductCallback(ecsLauncherViewModel)

    var ecsProductListCallback = ECSProductListCallback(ecsLauncherViewModel)

    fun configECS() {
        ecsServices.configureECS(configBooleanCallback)
    }

    fun configECSToGetConfig() {
        ecsServices.configureECSToGetConfiguration(configurationCallback)
    }

    fun getProductDetailForCtn(ctn: String) {
        ecsServices.fetchProduct(ctn, ecsProductCallback)
    }

    fun getRetailerProductDetailForCtn(ctn: String) {
        ecsServices.fetchProductSummaries(Arrays.asList(ctn), ecsProductListCallback)
    }
}