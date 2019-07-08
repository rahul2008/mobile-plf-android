package com.ecs.demouapp.ui.response.carts;

/**
 * Created by 310228564 on 2/9/2016.
 */
public class UpdateCartData {
    private Entry entry;
    private int quantity;
    private int quantityAdded;
    private String statusCode;


    public Entry getEntry() {
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

    @Override
    public String toString() {
        return super.toString();
    }
}
