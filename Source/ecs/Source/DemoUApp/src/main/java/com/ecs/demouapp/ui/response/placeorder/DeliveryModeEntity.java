package com.ecs.demouapp.ui.response.placeorder;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryModeEntity {
    private String code;

    private DeliveryCostEntity deliveryCost;
    private String description;
    private String name;

    public String getCode() {
        return code;
    }

    public DeliveryCostEntity getDeliveryCost() {
        return deliveryCost;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
