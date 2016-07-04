/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.response.addresses;

public class DeliveryModes {
    private String code;
    private DeliveryCost deliveryCost;
    private String description;
    private String name;

    public void setCode(String code) {
        this.code = code;
    }

    public void setDeliveryCost(DeliveryCost deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public DeliveryCost getDeliveryCost() {
        return deliveryCost;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
