package com.philips.cdp.di.mec.payment

import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.utils.MECDataHolder

class PaymentViewModel : CommonViewModel() {

    var mecPayments = MutableLiveData<MECPayments>()

    var ecsOrderDetail = MutableLiveData<ECSOrderDetail>()

    var ecsServices = MECDataHolder.INSTANCE.eCSServices

    var paymentListCallback = PaymentListCallback(this)

    var submitOrderCallback = SubmitOrderCallback(this)

    var paymentRepository = PaymentRepository(ecsServices)


    fun fetchPaymentDetails(){
        paymentRepository.fetchPaymentDetails(paymentListCallback)
    }

    fun submitOrder(cvv :String){
        paymentRepository.submitOrder(cvv,submitOrderCallback)
    }

}