package com.philips.cdp.di.ecs.model.orders;

import com.philips.cdp.di.ecs.model.products.ECSProduct;

public class Entries {
    private int entryNumber;

    private ECSProduct product;
    private int quantity;

    private Cost totalPrice;



    public int getEntryNumber() {
        return entryNumber;
    }

    public ECSProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Cost getTotalPrice() {
        return totalPrice;
    }

}
