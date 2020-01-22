package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.MecError

class ECSShoppingCartCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel) : ECSCallback<ECSShoppingCart, Exception> {
    /**
     * On response.
     *
     * @param result the result
     */
    override fun onResponse(result: ECSShoppingCart?) {
        ecsProductDetailViewModel.ecsShoppingcart.value=result
    }

    /**
     * On failure.
     * @param error     the error object
     * @param ecsError the error code
     */
    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsProductDetailViewModel.mecError.value = mecError
    }
}