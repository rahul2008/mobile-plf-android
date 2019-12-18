package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError

class ECSProductDetailCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel)  : ECSCallback<ECSProduct, Exception> {

    override fun onResponse(ecsProduct: ECSProduct?) {
        ecsProductDetailViewModel.ecsProduct.value = ecsProduct
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsProductDetailViewModel.mecError.value = mecError
    }
}