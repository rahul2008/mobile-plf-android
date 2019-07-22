/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.products;

import com.google.gson.annotations.Expose;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.summary.Data;

import java.io.Serializable;

public class Product implements Serializable {

    private boolean availableForPickup;
    private String code;
    private DiscountPriceEntity discountPrice;
    private String name;
    private PriceEntity price;
    private PriceRangeEntity priceRange;
    private StockEntity stock;
    private String url;

    private Data summary;

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public com.philips.cdp.di.ecs.model.disclaimer.Disclaimers getDisclaimers() {
        return Disclaimers;
    }

    public void setDisclaimers(com.philips.cdp.di.ecs.model.disclaimer.Disclaimers disclaimers) {
        Disclaimers = disclaimers;
    }

    private Assets assets;
    private Disclaimers Disclaimers;

    public boolean isAvailableForPickup() {
        return availableForPickup;
    }

    public String getCode() {
        return code;
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

    public void setCode(String productCode) {
        this.code = productCode;
    }
}
