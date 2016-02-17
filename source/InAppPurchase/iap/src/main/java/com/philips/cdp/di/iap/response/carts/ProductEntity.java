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
    /**
     * stockLevel : 902
     * stockLevelStatus : inStock
     */

    private StockEntity stock;
    private String url;
    /**
     * code : Tuscany_Campaign
     * url : /Tuscany-Category/c/Tuscany_Campaign
     */

    private List<CategoriesEntity> categories;

    public void setAvailableForPickup(boolean availableForPickup) {
        this.availableForPickup = availableForPickup;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategories(List<CategoriesEntity> categories) {
        this.categories = categories;
    }

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

    public StockEntity getStock() {
        return stock;
    }

    public String getUrl() {
        return url;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }
}
