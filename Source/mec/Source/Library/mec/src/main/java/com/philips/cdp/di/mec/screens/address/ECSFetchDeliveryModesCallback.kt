package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.utils.MECutility

class ECSFetchDeliveryModesCallback(private val addressViewModel: AddressViewModel) : ECSCallback<List<ECSDeliveryMode>, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(ecsDeliveryModes: List<ECSDeliveryMode>?) {
        addressViewModel.ecsDeliveryModes.value=ecsDeliveryModes
    }


    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        if (MECutility.isAuthError(ecsError)) {
            addressViewModel.retryAPI(mECRequestType)
        }else{
            addressViewModel.mecError.value = mecError
        }
    }
}