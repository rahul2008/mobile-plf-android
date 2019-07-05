/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.carts;
public class Price {
    private String currencyIso;
    private double value;

    public String getCurrencyIso() {
        return currencyIso;
    }

    public double getValue() {
        return value;
    }
}
