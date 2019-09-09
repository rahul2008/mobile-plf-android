package com.philips.cdp.di.ecs.model.orders;

import com.philips.cdp.di.ecs.model.products.ECSProduct;

/**
 * Created by philips on 5/15/19.
 */

public class OrderEntry {

    private ECSProduct product;

    public ECSProduct getProduct() {
        return product;
    }

    public void setProduct(ECSProduct product) {
        this.product = product;
    }
}
