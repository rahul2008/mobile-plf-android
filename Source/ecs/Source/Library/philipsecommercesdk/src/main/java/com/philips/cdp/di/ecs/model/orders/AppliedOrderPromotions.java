package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;

public class AppliedOrderPromotions implements Serializable {
    private String description;
    private Promotion promotion;

    public String getDescription() {
        return description;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
