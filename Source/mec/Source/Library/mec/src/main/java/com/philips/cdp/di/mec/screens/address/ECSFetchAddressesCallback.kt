package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.shoppingCart.EcsShoppingCartViewModel

class ECSFetchAddressesCallback (private val addressViewModel: AddressViewModel)  : ECSCallback<List<ECSAddress>, Exception> {

    var mECRequestType = MECRequestType.MEC_FETCH_SAVED_ADDRESSES
    override fun onResponse(ecsAddresses: List<ECSAddress>) {
        addressViewModel.ecsAddresses.value = ecsAddresses
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        addressViewModel.mecError.value = mecError
    }
}