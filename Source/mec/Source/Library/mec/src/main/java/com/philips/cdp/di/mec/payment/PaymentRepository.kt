package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider

class PaymentRepository(val ecsServices: ECSServices) {


    fun fetchPaymentDetails(paymentListCallback: PaymentListCallback){
        ecsServices.fetchPaymentsDetails(paymentListCallback)
    }

    fun submitOrder(cvv : String , submitOrderCallback: SubmitOrderCallback) {
        ecsServices.submitOrder(cvv,submitOrderCallback)
    }

    fun makePayment(orderDetail: ECSOrderDetail, billingAdress: ECSAddress, makePaymentCallback : ECSCallback<ECSPaymentProvider, Exception>  ){
        ecsServices.makePayment(orderDetail,billingAdress,makePaymentCallback )
    }

}