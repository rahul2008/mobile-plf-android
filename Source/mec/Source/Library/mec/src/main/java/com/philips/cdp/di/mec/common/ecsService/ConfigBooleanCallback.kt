package com.philips.cdp.di.mec.common.ecsService

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import com.philips.cdp.di.mec.common.MecError

class ConfigBooleanCallback(private val ecsLauncherViewModel: EcsLauncherViewModel) : ECSCallback<Boolean, Exception> {


    override fun onResponse(result: Boolean?) {
        ecsLauncherViewModel.isHybris.value = result
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsLauncherViewModel.mecError.value = mecError
    }
}