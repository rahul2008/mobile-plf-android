package com.philips.cdp.di.mec.screens.features

import com.philips.cdp.prxclient.datamodels.features.FeaturesModel
import com.philips.cdp.prxclient.error.PrxError
import com.philips.cdp.prxclient.response.ResponseData
import com.philips.cdp.prxclient.response.ResponseListener

class PRXProductFeaturesResponseCallback(private val productFeaturesViewModel: ProductFeaturesViewModel) : ResponseListener {

    override fun onResponseSuccess(responseData: ResponseData?) {
        productFeaturesViewModel.features.value = responseData as FeaturesModel
    }

    override fun onResponseError(prxError: PrxError?) {
        TODO("not implemented")
        val description = prxError?.description
    }
}