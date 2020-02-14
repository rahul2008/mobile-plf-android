package com.philips.cdp.di.mec.screens.shoppingCart

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.utils.MECutility

class ECSShoppingCartCallback(private val ecsShoppingCartViewModel: EcsShoppingCartViewModel) : ECSCallback<ECSShoppingCart, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(ecsShoppingCart: ECSShoppingCart?) {
        ecsShoppingCartViewModel.ecsShoppingCart.value = ecsShoppingCart
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)

        if (MECutility.isAuthError(ecsError)) {
            ecsShoppingCartViewModel.retryAPI(mECRequestType)
        } else if (ecsError!!.errorcode == ECSErrorEnum.ECSCartError.errorCode) {
            ecsShoppingCartViewModel.createShoppingCart("")
        } else {
            ecsShoppingCartViewModel.mecError.value = mecError
        }

    }
}