package com.philips.cdp.di.mec.screens.shoppingCart

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.MecError

class ECSFetchAddressesCallback (private val ecsShoppingCartViewModel: EcsShoppingCartViewModel)  : ECSCallback<List<ECSAddress>, Exception> {

    override fun onResponse(ecsAddresses: List<ECSAddress>) {
        ecsShoppingCartViewModel.ecsAddresses.value = ecsAddresses
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        ecsShoppingCartViewModel.mecError.value = mecError
    }
}