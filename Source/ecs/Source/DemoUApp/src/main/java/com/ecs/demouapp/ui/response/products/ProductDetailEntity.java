package com.ecs.demouapp.ui.response.products;

import java.util.List;

/**
 * Created by 310241054 on 6/21/2016.
 */
public class ProductDetailEntity {


    private boolean availableForPickup;
    private String code;
    private String description;

    private DiscountPriceEntity discountPrice;
    private int numberOfReviews;

    private PriceEntity price;
    private PriceRangeEntity priceRange;
    private boolean purchasable;

    private StockBeanEntity stock;
    private String summary;
    private String url;

    private List<CategoriesEntity> categories;

    public boolean isAvailableForPickup() {
        return availableForPickup;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public DiscountPriceEntity getDiscountPrice() {
        return discountPrice;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public PriceEntity getPrice() {
        return price;
    }

    public PriceRangeEntity getPriceRange() {
        return priceRange;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public StockBeanEntity getStock() {
        return stock;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }

}
