package com.philips.cdp.di.ecs.model.cart;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductEntity {
    private boolean availableForPickup;
    private String code;

    private DiscountPriceEntity discountPrice;
    private boolean purchasable;

    private StockEntity stock;
    private String url;

    private List<CategoriesEntity> categories;

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

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }
}
