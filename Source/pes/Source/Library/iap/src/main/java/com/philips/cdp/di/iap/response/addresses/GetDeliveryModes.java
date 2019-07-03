/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.response.addresses;

import java.util.List;

public class GetDeliveryModes {

    private List<DeliveryModes> deliveryModes;

    public void setDeliveryModes(List<DeliveryModes> deliveryModes) {
        this.deliveryModes = deliveryModes;
    }

    public List<DeliveryModes> getDeliveryModes() {
        return deliveryModes;
    }
}
