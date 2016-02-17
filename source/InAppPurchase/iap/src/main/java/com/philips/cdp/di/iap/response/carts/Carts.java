package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Carts {
    private List<CartsEntity> carts;

    public void setCarts(List<CartsEntity> carts) {
        this.carts = carts;
    }

    public List<CartsEntity> getCarts() {
        return carts;
    }
}
