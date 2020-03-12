package com.philips.cdp.di.mec.payment

import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.utils.MECDataHolder

class PaymentViewModel : CommonViewModel() {

    var paymentList = MutableLiveData<List<MECPayment>>()

    var ecsServices = MECDataHolder.INSTANCE.eCSServices

    var paymentListCallback = PaymentListCallback(this)

    var paymentRepository = PaymentRepository(ecsServices)


    fun fetchPaymentDetails(){
        paymentRepository.fetchPaymentDetails(paymentListCallback)
    }

}