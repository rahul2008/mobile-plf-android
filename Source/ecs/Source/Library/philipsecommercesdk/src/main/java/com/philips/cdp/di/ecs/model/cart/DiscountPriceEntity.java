package com.philips.cdp.di.ecs.model.cart;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class DiscountPriceEntity {
    private String currencyIso;
    private double value;

    public String getCurrencyIso() {
        return currencyIso;
    }

    public double getValue() {
        return value;
    }
}
