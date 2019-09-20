/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers;

import java.io.Serializable;

public class OnlineStoresForProductEntity implements Serializable {
    private String excludePhilipsShopInWTB;
    private String showPrice;
    private String ctn;
    private ECSRetailers Stores;

    public String getExcludePhilipsShopInWTB() {
        return excludePhilipsShopInWTB;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public String getCtn() {
        return ctn;
    }

    public ECSRetailers getStores() {
        return Stores;
    }
}
