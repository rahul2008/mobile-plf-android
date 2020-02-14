package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.region.ECSRegion
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class ECSRegionListCallback(private var addressViewModel: AddressViewModel) : ECSCallback<List<ECSRegion>, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(result: List<ECSRegion>?) {
        addressViewModel.regionsList.value = result
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        addressViewModel.mecError.value = mecError
    }
}