package com.philips.cdp.prodreg.launcher;

import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegLaunchInput extends UappLaunchInput {
    private ArrayList<Product> products;
    private boolean isAppLaunch;
    private ProdRegUiListener prodRegUiListener;

    public ProdRegLaunchInput(final ArrayList<Product> products, final boolean isAppLaunch) {
        this.products = products;
        this.isAppLaunch = isAppLaunch;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean isAppLaunch() {
        return isAppLaunch;
    }

    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    public void setProdRegUiListener(final ProdRegUiListener prodRegUiListener) {
        this.prodRegUiListener = prodRegUiListener;
    }
}
