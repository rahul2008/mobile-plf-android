package com.philips.cdp.di.ecs.model.cart;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class StockEntity implements Serializable {
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

