/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.response.cart;
public class Product {
    private boolean availableForPickup;
    private String code;
    private String name;
    private boolean purchasable;
    private Stock stock;
    private String url;

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
}
