package com.philips.cdp.di.iap.response.cart;

import java.util.List;

public class Carts {
    private String code;
    private String guid;
    private int totalItems;
    private Price totalPrice;
    private Price totalPriceWithTax;
    private List<Entries> entries;

    public String getCode() {
        return code;
    }

    public String getGuid() {
        return guid;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public Price getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<Entries> getEntries() {
        return entries;
    }
}
