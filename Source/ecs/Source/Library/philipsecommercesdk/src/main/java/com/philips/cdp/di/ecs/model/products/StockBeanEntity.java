package com.philips.cdp.di.ecs.model.products;

import java.io.Serializable;

public class StockBeanEntity implements Serializable {
    private int stockLevel;
    private String stockLevelStatus;

    public int getStockLevel() {
        return stockLevel;
    }

    public String getStockLevelStatus() {
        return stockLevelStatus;
    }
}
