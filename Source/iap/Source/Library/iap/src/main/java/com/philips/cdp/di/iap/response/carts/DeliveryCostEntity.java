package com.philips.cdp.di.iap.response.carts;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryCostEntity implements Serializable {


    private static final long serialVersionUID = -294891733157681685L;
    private String currencyIso;

    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

    private String formattedValue;
    private String priceType;
    private double value;

    public String getCurrencyIso() {
        return currencyIso;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public String getPriceType() {
        return priceType;
    }

    public double getValue() {
        return value;
    }
}