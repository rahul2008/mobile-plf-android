/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.cart;

import java.util.List;

public class Carts {

    public Carts(List<ECSShoppingCart> cartsList) {
        carts = cartsList;
    }

    private List<ECSShoppingCart> carts;

    public List<ECSShoppingCart> getCarts() {
        return carts;
    }
}
