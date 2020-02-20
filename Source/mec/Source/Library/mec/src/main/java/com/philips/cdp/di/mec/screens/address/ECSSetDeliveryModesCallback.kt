package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode

class ECSSetDeliveryModesCallback(private val addressViewModel: AddressViewModel) : ECSCallback<Boolean, Exception> {

    override fun onResponse(result: Boolean?) {

    }


    override fun onFailure(error: Exception?, ecsError: ECSError?) {

    }
}