package com.ecs.demouapp.ui.response.orders;

public class Product {
    private boolean availableForPickup;
    private String code;
    private boolean purchasable;

    private Stock stock;
    private String url;

    public boolean isAvailableForPickup() {
        return availableForPickup;
    }

    public String getCode() {
        return code;
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