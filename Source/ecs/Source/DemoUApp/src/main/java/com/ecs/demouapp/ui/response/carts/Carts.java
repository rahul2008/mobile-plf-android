/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.response.carts;

import java.util.List;

public class Carts {

    public Carts(List<CartsEntity> cartsList) {
        carts = cartsList;
    }

    private List<CartsEntity> carts;

    public List<CartsEntity> getCarts() {
        return carts;
    }
}
