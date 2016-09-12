package com.philips.cdp.prodreg.launcher;

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
    /* private
   @IdRes
    int firstScreenImageResourceId;
    private
    @IdRes
    int otherScreenImageResourceId;*/

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

   /* public int getFirstScreenImageResourceId() {
        return firstScreenImageResourceId;
    }

    public void setFirstScreenImageResourceId(final int firstScreenImageResourceId) {
        this.firstScreenImageResourceId = firstScreenImageResourceId;
    }

    public int getOtherScreenImageResourceId() {
        return otherScreenImageResourceId;
    }

    public void setOtherScreenImageResourceId(final int otherScreenImageResourceId) {
        this.otherScreenImageResourceId = otherScreenImageResourceId;
    }*/
}
