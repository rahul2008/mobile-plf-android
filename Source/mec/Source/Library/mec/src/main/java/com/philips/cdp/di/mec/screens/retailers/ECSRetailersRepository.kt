package com.philips.cdp.di.mec.screens.retailers

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder

class ECSRetailersRepository {

    fun getRetailers(ctn: String, ecsRetailerViewModel: ECSRetailerViewModel) {

        val ecsServices = MecHolder.INSTANCE.eCSServices
        ecsServices.fetchRetailers(ctn, object : ECSCallback<ECSRetailerList, Exception> {

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsRetailerViewModel.mecError.value = mecError
            }

            override fun onResponse(ecsRetailers: ECSRetailerList) {
                ecsRetailerViewModel.ecsRetailerList.value = ecsRetailers
            }

        })
    }

}