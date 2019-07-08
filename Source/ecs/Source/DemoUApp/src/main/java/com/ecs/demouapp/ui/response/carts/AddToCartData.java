/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.response.carts;

public class AddToCartData {

    private EntriesEntity entry;
    private int quantity;
    private int quantityAdded;
    private String statusCode;

    public EntriesEntity getEntry() {
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
