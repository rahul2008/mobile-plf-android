package com.philips.cdp.di.ecs.model.payment;

import com.philips.cdp.di.ecs.model.address.ECSAddress;

import java.io.Serializable;

public class ECSPayment implements Serializable {

    private static final long serialVersionUID = 1083630169028052247L;
    private String accountHolderName;

    public void setBillingAddress(ECSAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    private ECSAddress billingAddress;
    private String cardNumber;
    private CardType cardType;
    private boolean defaultPayment;
    private String expiryMonth;
    private String expiryYear;
    private String id;
    private boolean saved;
    private String subscriptionId;

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public ECSAddress getBillingAddress() {
        return billingAddress;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean isDefaultPayment() {
        return defaultPayment;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public String getId() {
        return id;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}
