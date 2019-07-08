package com.ecs.demouapp.ui.response.payment;



import com.ecs.demouapp.ui.response.addresses.Addresses;

import java.io.Serializable;

public class PaymentMethod implements Serializable{

    private static final long serialVersionUID = 1083630169028052247L;
    private String accountHolderName;

    public void setBillingAddress(Addresses billingAddress) {
        this.billingAddress = billingAddress;
    }

    private Addresses billingAddress;
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

    public Addresses getBillingAddress() {
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
