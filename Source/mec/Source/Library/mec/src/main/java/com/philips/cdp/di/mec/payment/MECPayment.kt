package com.philips.cdp.di.mec.payment

import com.philips.cdp.di.ecs.model.payment.ECSPayment
import java.io.Serializable

class MECPayment (var ecsPayment: ECSPayment): Serializable{
    var isSelected: Boolean =false
}