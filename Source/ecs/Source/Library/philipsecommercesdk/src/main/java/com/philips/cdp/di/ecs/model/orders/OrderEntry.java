package com.philips.cdp.di.ecs.model.orders;

import com.philips.cdp.di.ecs.model.products.ECSProduct;

import java.io.Serializable;

/**
 * Created by philips on 5/15/19.
 */

public class OrderEntry implements Serializable {

    private ECSProduct product;

    public ECSProduct getProduct() {
        return product;
    }

    public void setProduct(ECSProduct product) {
        this.product = product;
    }
}
