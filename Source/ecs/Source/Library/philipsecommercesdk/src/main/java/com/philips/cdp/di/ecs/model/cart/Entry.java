package com.philips.cdp.di.ecs.model.cart;

import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.summary.Price;

/**
 * Created by 310228564 on 2/9/2016.
 */
public class Entry {
    private int entryNumber;
    private ECSProduct product;
    private int quantity;
    private Price totalPrice;

    public int getEntryNumber() {
        return entryNumber;
    }

    public ECSProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }
}
