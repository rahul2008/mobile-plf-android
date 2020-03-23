package com.philips.cdp.di.mec.paymentServices

import com.philips.cdp.di.mec.utils.MECConstant


class MECPayments(val payments:MutableList<MECPayment> , var isPaymentDownloaded: Boolean ){


    fun isNewCardPresent() : Boolean{

        for (payment in payments){

            if(payment.ecsPayment.id.equals(MECConstant.NEW_CARD_PAYMENT,true)){
                return true
            }
        }
        return false
    }

    fun getNewCard() : MECPayment ?{

        for (payment in payments){

            if(payment.ecsPayment.id.equals(MECConstant.NEW_CARD_PAYMENT,true)){
                return payment
            }
        }
        return null
    }

    fun getSelectedPayment() : MECPayment ?{

        for (payment in payments){

            if(payment.isSelected){
                return payment
            }
        }
        return null
    }

    fun setSelection(selectedPayment: MECPayment){
        for (payment in payments){
            payment.isSelected = payment.ecsPayment.id.equals(selectedPayment.ecsPayment.id,true)
        }
    }
}