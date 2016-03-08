package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductEntity {
    private boolean availableForPickup;
    private String code;
    /**
     * currencyIso : USD
     * value : 400
     */

    private DiscountPriceEntity discountPrice;
    private boolean purchasable;
    /**
     * stockLevel : 937
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

    public void setDiscountPrice(DiscountPriceEntity discountPrice) {
        this.discountPrice = discountPrice;
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

    public DiscountPriceEntity getDiscountPrice() {
        return discountPrice;
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
