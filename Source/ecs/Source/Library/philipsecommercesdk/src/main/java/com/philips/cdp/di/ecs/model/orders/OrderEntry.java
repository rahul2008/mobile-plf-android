package com.philips.cdp.di.ecs.model.orders;

import com.philips.cdp.di.ecs.model.products.Product;

/**
 * Created by philips on 5/15/19.
 */

public class OrderEntry {

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
