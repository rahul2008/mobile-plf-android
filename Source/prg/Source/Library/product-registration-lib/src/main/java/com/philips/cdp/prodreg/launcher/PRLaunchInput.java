package com.philips.cdp.prodreg.launcher;

import android.support.annotation.IdRes;

import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * This class is used to provide input parameters and customizations for Product registration.
 *
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
     * @param products - ArrayList<Product> products
     * @param isAppLaunchFlow - boolean isAppLaunchFlow
     * @since 1.0.0
     */
    public PRLaunchInput(final ArrayList<Product> products, final boolean isAppLaunchFlow) {
        this.products = products;
        this.isAppLaunchFlow = isAppLaunchFlow;
    }

    /**
     * API returns products
     * @return Products
     * @since 1.0.0
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * API returns APP launch flow or not
     * @return boolean for AppLaunchFlow or not
     * @since 1.0.0
     */
    public boolean isAppLaunchFlow() {
        return isAppLaunchFlow;
    }

    /**
     * API returns ProdRegUiListener instance
     * @return ProdRegUiListener
     * @since 1.0.0
     */
    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    /**
     * API sets ProdRegUiListener instance
     * @param prodRegUiListener - ProdRegUiListener prodRegUiListener
     * @since 1.0.0
     */
    public void setProdRegUiListener(final ProdRegUiListener prodRegUiListener) {
        this.prodRegUiListener = prodRegUiListener;
    }

    /**
     * API returns BackgroundImageResourceId
     * @return BackgroundImageResourceId in Integer
     * @since 1.0.0
     */
    public int getBackgroundImageResourceId() {
        return backgroundImageResourceId;
    }

    /**
     * API sets BackgroundImageResourceId
     * @param backgroundImageResourceId - int backgroundImageResourceId
     * @since 1.0.0
     */
    public void setBackgroundImageResourceId(final int backgroundImageResourceId) {
        this.backgroundImageResourceId = backgroundImageResourceId;
    }
}
