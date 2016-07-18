package com.philips.cdp.prodreg.launcher;

import com.philips.cdp.prodreg.register.Product;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegConfig {
    private ArrayList<Product> products;
    private boolean isFirstLaunch;

    public ProdRegConfig(final ArrayList<Product> products, final boolean isFirstLaunch) {
        this.products = products;
        this.isFirstLaunch = isFirstLaunch;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }
}
