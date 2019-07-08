package com.ecs.demouapp.ui.response.carts;

/**
 * Created by 310228564 on 2/9/2016.
 */
public class Entry {
    private int entryNumber;
    private ProductEntity product;
    private int quantity;
    private Price totalPrice;

    public int getEntryNumber() {
        return entryNumber;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }
}
