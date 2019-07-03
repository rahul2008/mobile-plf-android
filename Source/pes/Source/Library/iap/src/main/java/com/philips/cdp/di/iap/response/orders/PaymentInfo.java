package com.philips.cdp.di.iap.response.orders;

public class PaymentInfo {


    private Address billingAddress;
    private String cardNumber;
    private CardType cardType;
    private boolean defaultPayment;
    private String expiryMonth;
    private String expiryYear;
    private boolean saved;

    public Address getBillingAddress() {
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

    public boolean isSaved() {
        return saved;
    }


}
