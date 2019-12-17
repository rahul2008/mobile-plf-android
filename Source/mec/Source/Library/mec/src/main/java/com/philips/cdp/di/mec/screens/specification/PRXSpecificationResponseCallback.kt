package com.philips.cdp.di.mec.screens.specification

import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel
import com.philips.cdp.prxclient.error.PrxError
import com.philips.cdp.prxclient.response.ResponseData
import com.philips.cdp.prxclient.response.ResponseListener

class PRXSpecificationResponseCallback(private val prxSpecificationViewModel: SpecificationViewModel) : ResponseListener {

    override fun onResponseSuccess(responseData: ResponseData?) {
        prxSpecificationViewModel.specification.value = responseData as SpecificationModel
    }

    override fun onResponseError(prxError: PrxError?) {
        TODO("not implemented")
        val description = prxError?.description
    }
}