package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.util.ECSConfiguration

class AddressRepository(val ecsServices: ECSServices) {


    fun getRegions(ecsRegionListCallback: ECSRegionListCallback){
        ecsServices.fetchRegions(ECSConfiguration.INSTANCE.country,ecsRegionListCallback)
    }

    fun createAddress(ecsAddress: ECSAddress , ecsCreateAddressCallBack :ECSCreateAddressCallBack) {
        ecsServices.createAddress(ecsAddress,ecsCreateAddressCallBack)
    }
}