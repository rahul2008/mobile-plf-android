package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.MecError

class MECAddToProductCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel, private val request :String) : ECSCallback<ECSShoppingCart, Exception> {
    /**
     * On response.
     *
     * @param result the result
     */
    override fun onResponse(result: ECSShoppingCart?) {
        ecsProductDetailViewModel.addToProductCallBack.onResponse(result)
    }

    /**
     * On failure.
     * @param error     the error object
     * @param ecsError the error code
     */
    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)

        if (       ecsError!!.errorcode == ECSErrorEnum.ECSInvalidTokenError.errorCode
                || ecsError!!.errorcode == ECSErrorEnum.ECSinvalid_grant.errorCode
                || ecsError!!.errorcode == ECSErrorEnum.ECSinvalid_client.errorCode) {
            ecsProductDetailViewModel.retryFunction()
        }else if (ecsError!!.errorcode == ECSErrorEnum.ECSCartError.errorCode){
            ecsProductDetailViewModel.createShoppingCart(request)
        } else{

            ecsProductDetailViewModel.addToProductCallBack.onFailure(error, ecsError)
        }
    }
}