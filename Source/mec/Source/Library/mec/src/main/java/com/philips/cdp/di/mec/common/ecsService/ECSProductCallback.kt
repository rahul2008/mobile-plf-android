package com.philips.cdp.di.mec.common.ecsService

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import com.philips.cdp.di.mec.common.MecError

class ECSProductCallback (private val ecsLauncherViewModel: EcsLauncherViewModel): ECSCallback<ECSProduct, Exception> {

    override fun onResponse(result: ECSProduct?) {
        ecsLauncherViewModel.ecsProduct.value = result
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsLauncherViewModel.mecError.value = mecError
    }
}