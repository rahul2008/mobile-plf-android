package com.philips.cdp.di.iap.Response.Cart;
/**
 * Created by 310228564 on 2/2/2016.
 */
public class Product {
    private boolean availableForPickup;
    private String code;
    private String name;
    private boolean purchasable;
    private Stock stock;
    private String url;

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

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Stock getStock() {
        return stock;
    }

    public String getUrl() {
        return url;
    }
}
