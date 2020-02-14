package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class ECSProductForCTNCallback (private val ecsProductDetailViewModel: EcsProductDetailViewModel): ECSCallback<ECSProduct, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(result: ECSProduct?) {
        ecsProductDetailViewModel.ecsProduct.value = result
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        ecsProductDetailViewModel.mecError.value = mecError
    }
}