package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.ECSServices

class PaymentRepository(val ecsServices: ECSServices) {


    fun fetchPaymentDetails(paymentListCallback: PaymentListCallback){
        ecsServices.fetchPaymentsDetails(paymentListCallback)
    }

}