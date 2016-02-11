/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.cart;

public class Entries {
    private int entryNumber;
    private Product product;
    private int quantity;
    private Price totalPrice;

    public int getEntryNumber() {
        return entryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "entryNumber = " + entryNumber + " product =" + product + "quantity = " + quantity + "totalPrice" + totalPrice;
    }
}
