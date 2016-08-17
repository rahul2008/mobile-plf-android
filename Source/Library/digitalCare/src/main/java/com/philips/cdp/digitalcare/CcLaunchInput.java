package com.philips.cdp.digitalcare;

import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Created by 310190678 on 8/17/2016.
 */
public class CcLaunchInput extends UappLaunchInput {

    private ProductModelSelectionType productModelSelectionType = null;
    private CcListener consumerCareListener = null;

    public ProductModelSelectionType getProductModelSelectionType() {
        return productModelSelectionType;
    }

    public void setProductModelSelectionType(ProductModelSelectionType productModelSelectionType) {
        this.productModelSelectionType = productModelSelectionType;
    }

    public CcListener getConsumerCareListener() {
        return consumerCareListener;
    }

    public void setConsumerCareListener(CcListener consumerCareListener) {
        this.consumerCareListener = consumerCareListener;
    }
}
