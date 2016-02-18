/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.carts;
public class Price {
    private String currencyIso;
    private int value;

    public String getCurrencyIso() {
        return currencyIso;
    }

    public int getValue() {
        return value;
    }
}
