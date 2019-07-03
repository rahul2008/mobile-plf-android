package com.philips.cdp.di.iap.response.carts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class StockEntity {
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    private int stockLevel;
    private String stockLevelStatus;

    public int getStockLevel() {
        return stockLevel;
    }

    public String getStockLevelStatus() {
        return stockLevelStatus;
    }
}

