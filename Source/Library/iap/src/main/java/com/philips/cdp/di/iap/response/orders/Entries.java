package com.philips.cdp.di.iap.response.orders;

public class Entries {
    private int entryNumber;

    private Product product;
    private int quantity;

    private TotalPrice totalPrice;

    public int getEntryNumber() {
        return entryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public TotalPrice getTotalPrice() {
        return totalPrice;
    }



}
