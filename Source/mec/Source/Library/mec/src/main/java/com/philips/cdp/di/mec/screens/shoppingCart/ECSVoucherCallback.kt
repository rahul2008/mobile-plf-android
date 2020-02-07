package com.philips.cdp.di.mec.screens.shoppingCart

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher
import com.philips.cdp.di.mec.common.MecError

class ECSVoucherCallback(private var ecsShoppingCartViewModel: EcsShoppingCartViewModel) : ECSCallback<List<ECSVoucher>,Exception> {
    override fun onResponse(ecsVoucher: List<ECSVoucher>?) {
        ecsShoppingCartViewModel.getShoppingCart()
       // ecsShoppingCartViewModel.ecsVoucher.value = ecsVoucher
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsShoppingCartViewModel.mecError.value = mecError
    }
}