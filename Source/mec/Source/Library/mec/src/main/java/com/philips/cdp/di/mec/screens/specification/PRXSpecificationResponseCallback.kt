package com.philips.cdp.di.mec.screens.specification

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel
import com.philips.cdp.prxclient.error.PrxError
import com.philips.cdp.prxclient.response.ResponseData
import com.philips.cdp.prxclient.response.ResponseListener

class PRXSpecificationResponseCallback(private val prxSpecificationViewModel: SpecificationViewModel) : ResponseListener {
    var mECRequestType : MECRequestType?=null
    override fun onResponseSuccess(responseData: ResponseData?) {
        prxSpecificationViewModel.specification.value = responseData as SpecificationModel
    }

    override fun onResponseError(prxError: PrxError?) {

        val description = prxError?.description

        val exception = Exception(description)
        val ecsError = ECSError(1000,description)
        val mecError = MecError(exception, ecsError,mECRequestType)
        //prxSpecificationViewModel.mecError.value = mecError
    }
}