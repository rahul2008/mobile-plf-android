package com.philips.cdp.di.mec.paymentServices

import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.utils.MECDataHolder

class PaymentViewModel : CommonViewModel() {

    var mecPayments = MutableLiveData<MECPayments>()

    var ecsOrderDetail = MutableLiveData<ECSOrderDetail>()

    var eCSPaymentProvider = MutableLiveData<ECSPaymentProvider>()

    var ecsServices = MECDataHolder.INSTANCE.eCSServices

    var paymentListCallback = PaymentListCallback(this)

    var submitOrderCallback = SubmitOrderCallback(this)

    var makePaymentCallback = MakePaymentCallback(this)

    var paymentRepository = PaymentRepository(ecsServices)


    fun fetchPaymentDetails(){
        paymentListCallback.mECRequestType=MECRequestType.MEC_FETCH_PAYMENT_DETAILS
        paymentRepository.fetchPaymentDetails(paymentListCallback)
    }

    fun submitOrder(cvv :String?){
        submitOrderCallback.mECRequestType=MECRequestType.MEC_SUBMIT_ORDER
        paymentRepository.submitOrder(cvv,submitOrderCallback)
    }

    fun makePayment(orderDetail: ECSOrderDetail, billingAdress: ECSAddress){
        makePaymentCallback.mECRequestType=MECRequestType.MEC_MAKE_PAYMENT
        paymentRepository.makePayment(orderDetail,billingAdress,makePaymentCallback )
    }

}