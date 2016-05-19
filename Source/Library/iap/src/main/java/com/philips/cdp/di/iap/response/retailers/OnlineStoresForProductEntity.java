package com.philips.cdp.di.iap.response.retailers;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OnlineStoresForProductEntity {
    private String excludePhilipsShopInWTB;
    private String showPrice;
    private String ctn;
    private StoresEntity Stores;

    public String getExcludePhilipsShopInWTB() {
        return excludePhilipsShopInWTB;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public String getCtn() {
        return ctn;
    }

    public StoresEntity getStores() {
        return Stores;
    }
}
