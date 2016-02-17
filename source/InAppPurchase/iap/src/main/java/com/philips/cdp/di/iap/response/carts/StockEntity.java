package com.philips.cdp.di.iap.response.carts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class StockEntity {
    private int stockLevel;
    private String stockLevelStatus;

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setStockLevelStatus(String stockLevelStatus) {
        this.stockLevelStatus = stockLevelStatus;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public String getStockLevelStatus() {
        return stockLevelStatus;
    }
}
