package com.ecs.demouapp.ui.response.orders;

public class Entries {
    private int entryNumber;

    private Product product;
    private int quantity;

    private Cost totalPrice;



    public int getEntryNumber() {
        return entryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Cost getTotalPrice() {
        return totalPrice;
    }

}
