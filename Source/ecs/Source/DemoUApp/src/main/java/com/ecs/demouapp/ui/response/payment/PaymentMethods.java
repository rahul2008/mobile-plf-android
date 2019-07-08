package com.ecs.demouapp.ui.response.payment;

import java.util.List;

public class PaymentMethods {
    private List<PaymentMethod> payments;

    public List<PaymentMethod> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentMethod> payments) {
        this.payments = payments;
    }
}
