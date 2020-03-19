package com.philips.cdp.di.mec.screens.features

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel
import com.philips.cdp.prxclient.error.PrxError
import com.philips.cdp.prxclient.response.ResponseData
import com.philips.cdp.prxclient.response.ResponseListener

class PRXProductFeaturesResponseCallback(private val productFeaturesViewModel: ProductFeaturesViewModel) : ResponseListener {
     var mECRequestType : MECRequestType?=null
    override fun onResponseSuccess(responseData: ResponseData?) {
        productFeaturesViewModel.features.value = responseData as FeaturesModel
    }

    override fun onResponseError(prxError: PrxError?) {
        val description = prxError?.description

        val exception = Exception(description)
        val ecsError = ECSError(1000,description)
        val mecError = MecError(exception, ecsError,mECRequestType)
        //productFeaturesViewModel.mecError.value = mecError
    }
}