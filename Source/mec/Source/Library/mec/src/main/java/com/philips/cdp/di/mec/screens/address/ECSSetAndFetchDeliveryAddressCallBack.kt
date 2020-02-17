package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class ECSSetAndFetchDeliveryAddressCallBack(private var addressViewModel: AddressViewModel) :ECSCallback<Boolean, Exception> {
    var mECRequestType : MECRequestType?=null
    override fun onResponse(isSetDeliveryAddress: Boolean) {
       // addressViewModel.isDeliveryAddressSet.value = isSetDeliveryAddress
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        addressViewModel.mecError.value = mecError
    }
}