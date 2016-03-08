package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EntriesEntity {
    /**
     * currencyIso : USD
     * formattedValue : $400.00
     * priceType : BUY
     * value : 400
     */

    private BasePriceEntity basePrice;
    private int entryNumber;
    /**
     * availableForPickup : false
     * categories : [{"code":"Tuscany_Campaign","url":"/Tuscany-Category/c/Tuscany_Campaign"}]
     * code : HX9042/64
     * discountPrice : {"currencyIso":"USD","value":400}
     * purchasable : true
     * stock : {"stockLevel":937,"stockLevelStatus":"inStock"}
     * url : /Tuscany-Category//p/HX9042_64
     */

    private ProductEntity product;
    private int quantity;
    /**
     * currencyIso : USD
     * formattedValue : $800.00
     * priceType : BUY
     * value : 800
     */

    private TotalPriceEntity totalPrice;
    private boolean updateable;

    public BasePriceEntity getBasePrice() {
        return basePrice;
    }

    public int getEntryNumber() {
        return entryNumber;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public TotalPriceEntity getTotalPrice() {
        return totalPrice;
    }

    public boolean isUpdateable() {
        return updateable;
    }

}