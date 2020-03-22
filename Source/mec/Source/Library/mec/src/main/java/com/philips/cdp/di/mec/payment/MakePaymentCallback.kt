package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class MakePaymentCallback (private val paymentViewModel: PaymentViewModel): ECSCallback<ECSPaymentProvider, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(result: ECSPaymentProvider?) {
        paymentViewModel.eCSPaymentProvider.value=result
    }


    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        paymentViewModel.mecError.value = mecError
    }
}