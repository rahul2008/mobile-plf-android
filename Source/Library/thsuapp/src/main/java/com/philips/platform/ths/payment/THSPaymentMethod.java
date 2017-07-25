package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.billing.PaymentMethod;

/**
 * Created by philips on 7/21/17.
 */

public class THSPaymentMethod {
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    PaymentMethod paymentMethod;

}
