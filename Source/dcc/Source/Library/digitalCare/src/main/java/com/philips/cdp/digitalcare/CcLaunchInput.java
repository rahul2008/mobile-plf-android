package com.philips.cdp.digitalcare;

import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 *
 * class for getting inputs from vertical apps
 * Created by sampath.kumar on 8/17/2016.
 */
public class CcLaunchInput extends UappLaunchInput {

    private ProductModelSelectionType productModelSelectionType = null;
    private CcListener consumerCareListener = null;
    private String liveChatUrl = null;

    public ProductModelSelectionType getProductModelSelectionType() {
        return productModelSelectionType;
    }

    /**
     * sets the product model selection type with catalog, sector and ctn list.
     * @param productModelSelectionType
     * @since 1.0.0
     */
    public void setProductModelSelectionType(ProductModelSelectionType productModelSelectionType) {
        this.productModelSelectionType = productModelSelectionType;
    }

    public CcListener getConsumerCareListener() {
        return consumerCareListener;
    }

    /**
     * sets the listener for consumercare
     * @param consumerCareListener
     * @since 1.0.0
     */
    public void setConsumerCareListener(CcListener consumerCareListener) {
        this.consumerCareListener = consumerCareListener;
    }

    public String getLiveChatUrl() {
        return  liveChatUrl;
    }

    /**
     * sets the live chat url where vertical can customize the url
     * @param liveChatUrl
     * @since 1.0.0
     */
    public void setLiveChatUrl(String liveChatUrl) {
        this.liveChatUrl = liveChatUrl;
    }
}
