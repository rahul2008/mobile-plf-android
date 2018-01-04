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

    /**
     *
     * @param products
     * @param isAppLaunchFlow
     * @since 1.0.0
     */
    public PRLaunchInput(final ArrayList<Product> products, final boolean isAppLaunchFlow) {
        this.products = products;
        this.isAppLaunchFlow = isAppLaunchFlow;
    }

    /**
     *
     * @return Products
     * @since 1.0.0
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     *
     * @return boolean for AppLaunchFlow or not
     * @since 1.0.0
     */
    public boolean isAppLaunchFlow() {
        return isAppLaunchFlow;
    }

    /**
     *
     * @return ProdRegUiListener
     * @since 1.0.0
     */
    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    /**
     *
     * @param prodRegUiListener
     * @since 1.0.0
     */
    public void setProdRegUiListener(final ProdRegUiListener prodRegUiListener) {
        this.prodRegUiListener = prodRegUiListener;
    }

    /**
     *
     * @return BackgroundImageResourceId in Integer
     * @since 1.0.0
     */
    public int getBackgroundImageResourceId() {
        return backgroundImageResourceId;
    }

    /**
     *
     * @param backgroundImageResourceId
     * @since 1.0.0
     */
    public void setBackgroundImageResourceId(final int backgroundImageResourceId) {
        this.backgroundImageResourceId = backgroundImageResourceId;
    }
}
