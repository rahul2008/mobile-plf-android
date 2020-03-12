package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.mec.utils.MECConstant


class MECPayments(val payments:List<MECPayment> , var isPaymentDownloaded: Boolean ){


    fun isNewCardPresent() : Boolean{

        for (payment in payments){

            if(payment.ecsPayment.id.equals(MECConstant.NEW_CARD_PAYMENT,true)){
                return true
            }
        }
        return false
    }
}