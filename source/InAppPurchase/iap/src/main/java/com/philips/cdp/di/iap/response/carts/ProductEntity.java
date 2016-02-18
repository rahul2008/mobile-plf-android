package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductEntity {

    private boolean availableForPickup;
    private String code;
    private String name;
    private boolean purchasable;
    private Stock stock;
    private String url;

    private List<CategoriesEntity> categories;

    public boolean isAvailableForPickup() {
        return availableForPickup;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public Stock getStock() {
        return stock;
    }

    public String getUrl() {
        return url;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }
}
