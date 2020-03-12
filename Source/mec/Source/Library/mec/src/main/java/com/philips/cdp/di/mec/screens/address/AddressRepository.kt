package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode

class AddressRepository(val ecsServices: ECSServices) {


    fun fetchSavedAddresses( eCSFetchAddressesCallback: ECSFetchAddressesCallback) {
        ecsServices.fetchSavedAddresses(eCSFetchAddressesCallback)
    }

    fun createAddress(ecsAddress: ECSAddress , ecsCreateAddressCallBack :ECSCreateAddressCallBack) {
        ecsServices.createAddress(ecsAddress,ecsCreateAddressCallBack)
    }

    fun updateAndFetchAddress(ecsAddress: ECSAddress,ecsFetchAddressesCallback: ECSFetchAddressesCallback){
        ecsServices.updateAndFetchAddress(ecsAddress,ecsFetchAddressesCallback)
    }

    fun updateAddress(ecsAddress: ECSAddress,updateAddressCallBack: UpdateAddressCallBack){
        ecsServices.updateAddress(ecsAddress,updateAddressCallBack)
    }

    fun setAndFetchDeliveryAddress(ecsAddress: ECSAddress, ecsFetchAddressesCallback: ECSFetchAddressesCallback) {
        ecsServices.setAndFetchDeliveryAddress(true,ecsAddress,ecsFetchAddressesCallback)
    }

    fun setDeliveryAddress(ecsAddress: ECSAddress, setDeliveryAddressCallBack: SetDeliveryAddressCallBack ) {
        ecsServices.setDeliveryAddress(true,ecsAddress,setDeliveryAddressCallBack)
    }

    fun fetchDeliveryModes(eCSFetchDeliveryModesCallback :ECSFetchDeliveryModesCallback ){
        ecsServices.fetchDeliveryModes(eCSFetchDeliveryModesCallback)
    }

    fun setDeliveryMode(ecsDeliveryMode: ECSDeliveryMode, ecsSetDeliveryModesCallback: ECSSetDeliveryModesCallback){
        ecsServices.setDeliveryMode(ecsDeliveryMode,ecsSetDeliveryModesCallback)
    }

    fun createAndFetchAddress(ecsAddress: ECSAddress, ecsFetchAddressesCallback: ECSFetchAddressesCallback) {
        ecsServices.createAndFetchAddress(ecsAddress,ecsFetchAddressesCallback)
    }

    fun deleteAndFetchAddress(ecsAddress: ECSAddress, ecsFetchAddressesCallback: ECSFetchAddressesCallback){
        ecsServices.deleteAndFetchAddress(ecsAddress,ecsFetchAddressesCallback)
    }

    fun deleteAddress(ecsAddress: ECSAddress, deleteAddressCallBack: DeleteAddressCallBack) {
        ecsServices.deleteAddress(ecsAddress,deleteAddressCallBack)
    }


}