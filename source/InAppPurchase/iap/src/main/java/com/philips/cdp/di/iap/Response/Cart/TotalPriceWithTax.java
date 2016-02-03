package com.philips.cdp.di.iap.Response.Cart;

/**
 * Created by 310228564 on 2/3/2016.
 */
public class TotalPriceWithTax {
    private String currencyIso;
    private int value;

    public void setCurrencyIso(String currencyIso) {
        this.currencyIso = currencyIso;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrencyIso() {
        return currencyIso;
    }

    public int getValue() {
        return value;
    }
}
