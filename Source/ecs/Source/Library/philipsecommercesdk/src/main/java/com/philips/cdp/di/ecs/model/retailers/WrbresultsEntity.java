/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers;

public class WrbresultsEntity {
    private String Ctn;
    private String storeLocatorUrl;
    private String EloquaSiteURL;
    private String ShowBuyButton;
    private int EloquaSiteId;
    private boolean RetailStoreAvailableFlag;

    private TextsEntity Texts;
    private OnlineStoresForProductEntity OnlineStoresForProduct;

    public String getStoreLocatorUrl() {
        return storeLocatorUrl;
    }

    public String getEloquaSiteURL() {
        return EloquaSiteURL;
    }

    public String getShowBuyButton() {
        return ShowBuyButton;
    }

    public String getCtn() {
        return Ctn;
    }

    public int getEloquaSiteId() {
        return EloquaSiteId;
    }

    public boolean isRetailStoreAvailableFlag() {
        return RetailStoreAvailableFlag;
    }

    public TextsEntity getTexts() {
        return Texts;
    }

    public OnlineStoresForProductEntity getOnlineStoresForProduct() {
        return OnlineStoresForProduct;
    }


}
