package com.philips.cdp.di.ecs.model.payment;

import java.util.List;

public class PaymentMethods {
    private List<ECSPayment> payments;

    public List<ECSPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<ECSPayment> payments) {
        this.payments = payments;
    }
}
