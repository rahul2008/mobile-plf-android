package com.philips.cdp.di.iap.response.retailers;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WrbresultsEntity {
    private String storeLocatorUrl;
    private String EloquaSiteURL;
    private String ShowBuyButton;
    private TextsEntity Texts;
    private String Ctn;
    private OnlineStoresForProductEntity OnlineStoresForProduct;
    private int EloquaSiteId;
    private boolean RetailStoreAvailableFlag;

    public String getStoreLocatorUrl() {
        return storeLocatorUrl;
    }

    public String getEloquaSiteURL() {
        return EloquaSiteURL;
    }

    public String getShowBuyButton() {
        return ShowBuyButton;
    }

    public TextsEntity getTexts() {
        return Texts;
    }

    public String getCtn() {
        return Ctn;
    }

    public OnlineStoresForProductEntity getOnlineStoresForProduct() {
        return OnlineStoresForProduct;
    }

    public int getEloquaSiteId() {
        return EloquaSiteId;
    }

    public boolean isRetailStoreAvailableFlag() {
        return RetailStoreAvailableFlag;
    }
}
