package com.philips.cdp.di.iap.payment;

import java.io.Serializable;

public class PaymentMethodFields implements Serializable{

    private String cardName;
    private String cardHolderName;
    private String cardValidity;

    public String getCardName() {
        return cardName;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardValidity() {
        return cardValidity;
    }
}
