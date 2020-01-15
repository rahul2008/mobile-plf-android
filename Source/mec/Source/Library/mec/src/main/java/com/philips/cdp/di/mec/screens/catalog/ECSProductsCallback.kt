package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.common.MecError

class ECSProductsCallback(private var ecsProductViewModel:EcsProductViewModel) : ECSCallback<ECSProducts, Exception> {

    override fun onResponse(ecsProducts: ECSProducts?) {

        val mutableLiveData = ecsProductViewModel.ecsProductsList
        var value = mutableLiveData.value
        if (value.isNullOrEmpty()) value = mutableListOf<ECSProducts>()
        value?.add(ecsProducts!!)
        mutableLiveData.value = value
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsProductViewModel.mecError.value = mecError
    }
}