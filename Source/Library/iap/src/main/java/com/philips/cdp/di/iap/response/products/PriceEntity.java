package com.philips.cdp.di.iap.response.products;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PriceEntity {
    public String getCurrencyIso() {
        return currencyIso;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public String getPriceType() {
        return priceType;
    }

    public String getValue() {
        return value;
    }

    String currencyIso;
    String formattedValue;
    String priceType;
    String value;
}
