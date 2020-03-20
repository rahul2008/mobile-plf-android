package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class SubmitOrderCallback(private val paymentViewModel: PaymentViewModel) : ECSCallback<ECSOrderDetail, Exception>  {

    var mECRequestType = MECRequestType.MEC_FETCH_PAYMENT_DETAILS

    override fun onResponse(ecsOrderDetail: ECSOrderDetail?) {

        paymentViewModel.ecsOrderDetail.value = ecsOrderDetail
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {

        val mecError = MecError(error, ecsError,mECRequestType)
        paymentViewModel.mecError.value = mecError
    }
}