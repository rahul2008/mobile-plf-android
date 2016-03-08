package com.philips.cdp.di.iap.response.carts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryModeEntity {
    private String code;
    /**
     * currencyIso : USD
     * formattedValue : $8.99
     * priceType : BUY
     * value : 8.99
     */

    private DeliveryCostEntity deliveryCost;

    public void setCode(String code) {
        this.code = code;
    }

    public void setDeliveryCost(DeliveryCostEntity deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getCode() {
        return code;
    }

    public DeliveryCostEntity getDeliveryCost() {
        return deliveryCost;
    }


}
