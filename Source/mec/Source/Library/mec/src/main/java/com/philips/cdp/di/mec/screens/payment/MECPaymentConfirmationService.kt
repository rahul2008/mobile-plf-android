package com.philips.cdp.di.mec.screens.payment

import android.content.Context
import com.philips.cdp.di.mec.R

class MECPaymentConfirmationService {

    fun getTitle(paymentStatus: PaymentStatus, context: Context) : String{

        when(paymentStatus){

            PaymentStatus.SUCCESS -> "Success"
            else -> "fail"
        }
        return "fail"
    }


}