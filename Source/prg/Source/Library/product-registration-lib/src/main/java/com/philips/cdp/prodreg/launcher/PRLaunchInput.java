package com.philips.cdp.prodreg.launcher;

import androidx.annotation.IdRes;

import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;

/**
 * This class is used to provide input parameters and customizations for Product registration.
 * @since 1.0.0
 */
public class PRLaunchInput extends UappLaunchInput {
    private ArrayList<Product> products;
    private boolean isAppLaunchFlow;
    private ProdRegUiListener prodRegUiListener;
    private static final long serialVersionUID = -6635233525340545676L;

    private
    @IdRes
    int backgroundImageResourceId;

    boolean showExtendWarrantyNothanksButton;

    String mandatoryRegisterButtonText;

    /**
     *
     * @param products - pass list of products
     * @param isAppLaunchFlow - pass true then launch as app setup flow and if false then launch as user flow
     * @since 1.0.0
     */
    public PRLaunchInput(final ArrayList<Product> products, final boolean isAppLaunchFlow) {
        this.products = products;
        this.isAppLaunchFlow = isAppLaunchFlow;
    }

    /**
     * API returns products
     * @return instance of Products
     * @since 1.0.0
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * API returns APP launch flow or not
     * @return true then launch as app setup flow and if false then launch as user flow
     * @since 1.0.0
     */
    public boolean isAppLaunchFlow() {
        return isAppLaunchFlow;
    }

    /**
     * API returns ProdRegUiListener instance
     * @return instance of ProdRegUiListener
     * @since 1.0.0
     */
    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    /**
     * API sets ProdRegUiListener instance
     * @param prodRegUiListener pass instance of ProdRegUiListener
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
     * @param backgroundImageResourceId pass resource id of background image
     * @since 1.0.0
     */
    public void setBackgroundImageResourceId(final int backgroundImageResourceId) {
        this.backgroundImageResourceId = backgroundImageResourceId;
    }

    public boolean getMandatoryProductRegistration() {
        return showExtendWarrantyNothanksButton;
    }

    public void setMandatoryProductRegistration(boolean showExtendWarrntyNothanksButton) {
        this.showExtendWarrantyNothanksButton = showExtendWarrntyNothanksButton;
    }

    public String getMandatoryRegisterButtonText() {
        return mandatoryRegisterButtonText;
    }

    public void setMandatoryRegisterButtonText(String mandatoryRegisterButtonText) {
        this.mandatoryRegisterButtonText = mandatoryRegisterButtonText;
    }


}
