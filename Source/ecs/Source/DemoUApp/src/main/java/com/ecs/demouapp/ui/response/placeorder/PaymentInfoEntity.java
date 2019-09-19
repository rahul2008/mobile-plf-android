package com.ecs.demouapp.ui.response.placeorder;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PaymentInfoEntity {
    private String accountHolderName;

    private BillingAddressEntity billingAddress;
    private String cardNumber;

    private CardTypeEntity cardType;
    private boolean defaultPayment;
    private String expiryMonth;
    private String expiryYear;
    private String id;
    private boolean saved;
    private String subscriptionId;

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BillingAddressEntity getBillingAddress() {
        return billingAddress;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardTypeEntity getCardType() {
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
