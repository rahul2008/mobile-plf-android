package com.philips.cdp.di.ecs.model.cart;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductDiscountsEntity {
    private String currencyIso;
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
