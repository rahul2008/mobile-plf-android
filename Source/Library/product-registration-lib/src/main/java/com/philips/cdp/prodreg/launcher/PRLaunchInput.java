package com.philips.cdp.prodreg.launcher;

import android.support.annotation.IdRes;

import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRLaunchInput extends UappLaunchInput {
    private ArrayList<Product> products;
    private boolean isAppLaunchFlow;
    private ProdRegUiListener prodRegUiListener;
    private
    @IdRes
    int backgroundImageResourceId;

    public PRLaunchInput(final ArrayList<Product> products, final boolean isAppLaunchFlow) {
        this.products = products;
        this.isAppLaunchFlow = isAppLaunchFlow;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean isAppLaunchFlow() {
        return isAppLaunchFlow;
    }

    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    public void setProdRegUiListener(final ProdRegUiListener prodRegUiListener) {
        this.prodRegUiListener = prodRegUiListener;
    }

    public int getBackgroundImageResourceId() {
        return backgroundImageResourceId;
    }

    public void setBackgroundImageResourceId(final int backgroundImageResourceId) {
        this.backgroundImageResourceId = backgroundImageResourceId;
    }
}
