package com.philips.cdp.di.iap.response.voucher;

import java.io.Serializable;

public class AppliedValue implements Serializable {

    private static final long serialVersionUID = -2438759277104163832L;
    private String currencyIso;
    private String formattedValue;
    private String priceType;
    private String value;

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
}
