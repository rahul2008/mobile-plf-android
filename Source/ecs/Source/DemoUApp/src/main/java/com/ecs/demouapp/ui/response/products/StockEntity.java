/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.response.products;

public class StockEntity {

    private int stockLevel;
    private String stockLevelStatus;

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getStockLevelStatus() {
        return stockLevelStatus;
    }

    public void setStockLevelStatus(String stockLevelStatus) {
        this.stockLevelStatus = stockLevelStatus;
    }
}
