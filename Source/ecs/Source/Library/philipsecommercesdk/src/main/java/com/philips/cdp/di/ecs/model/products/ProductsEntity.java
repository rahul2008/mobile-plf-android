/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.products;

import com.philips.cdp.di.ecs.model.products.DiscountPriceEntity;
import com.philips.cdp.di.ecs.model.products.PriceEntity;
import com.philips.cdp.di.ecs.model.products.PriceRangeEntity;
import com.philips.cdp.di.ecs.model.summary.Data;

public class ProductsEntity {

    private boolean availableForPickup;
    private String code;
    private DiscountPriceEntity discountPrice;
    private String name;
    private PriceEntity price;
    private PriceRangeEntity priceRange;
    private StockEntity stock;
    private String url;
    private Data summary;

    public boolean isAvailableForPickup() {
        return availableForPickup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String ctn){
        code = ctn;
    }

    public DiscountPriceEntity getDiscountPrice() {
        return discountPrice;
    }

    public String getName() {
        return name;
    }

    public PriceEntity getPrice() {
        return price;
    }

    public PriceRangeEntity getPriceRange() {
        return priceRange;
    }

    public StockEntity getStock() {
        return stock;
    }

    public String getUrl() {
        return url;
    }

    public Data getSummary() {
        return summary;
    }

    public void setSummary(Data summary) {
        this.summary = summary;
    }
}
