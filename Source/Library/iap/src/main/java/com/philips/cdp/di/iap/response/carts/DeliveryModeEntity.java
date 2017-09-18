package com.philips.cdp.di.iap.response.carts;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryModeEntity implements Serializable{
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
