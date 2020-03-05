package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.payment.ECSPayment

class PaymentListCallback(val paymentViewModel: PaymentViewModel) : ECSCallback<List<ECSPayment>, Exception> {

    override fun onResponse(result: List<ECSPayment>?) {

        paymentViewModel.paymentList.value = result
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {

       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}