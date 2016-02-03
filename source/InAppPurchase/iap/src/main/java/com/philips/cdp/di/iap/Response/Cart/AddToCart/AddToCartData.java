package com.philips.cdp.di.iap.Response.Cart.AddToCart;

import com.philips.cdp.di.iap.Response.Cart.Entries;

/**
 * Created by 310228564 on 2/3/2016.
 */
public class AddToCartData {

    private Entries entry;
    private int quantity;
    private int quantityAdded;
    private String statusCode;

    public void setEntry(Entries entry) {
        this.entry = entry;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setQuantityAdded(int quantityAdded) {
        this.quantityAdded = quantityAdded;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Entries getEntry() {
        return entry;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    public String getStatusCode() {
        return statusCode;
    }

}
