package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class PaymentListCallback(private val paymentViewModel: PaymentViewModel) : ECSCallback<List<ECSPayment>, Exception> {

    var mECRequestType = MECRequestType.MEC_FETCH_PAYMENT_DETAILS

    override fun onResponse(payments: List<ECSPayment>?) {

        val mecPaymentList : MutableList<MECPayment> = mutableListOf()

        if (payments != null) {

            for (ecsPayment in payments){
                mecPaymentList.add(MECPayment(ecsPayment))
            }
        }

        val value = paymentViewModel.mecPayments.value
        value?.payments?.toMutableList()?.addAll(mecPaymentList)

        paymentViewModel.mecPayments.value = MECPayments(mecPaymentList,true)
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {

        val mecError = MecError(error, ecsError,mECRequestType)
        paymentViewModel.mecError.value = mecError
    }
}