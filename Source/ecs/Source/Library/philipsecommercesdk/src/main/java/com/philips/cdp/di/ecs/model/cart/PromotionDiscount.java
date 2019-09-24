package com.philips.cdp.di.ecs.model.cart;

import java.io.Serializable;

public class PromotionDiscount implements Serializable {

    String formattedValue;
    String priceType;

    public String getFormattedValue() {
        return formattedValue;
    }

    public String getPriceType() {
        return priceType;
    }
}
