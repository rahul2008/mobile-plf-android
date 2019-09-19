/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.address;

import java.util.List;

public class GetDeliveryModes {

    private List<ECSDeliveryMode> deliveryModes;

    public void setDeliveryModes(List<ECSDeliveryMode> deliveryModes) {
        this.deliveryModes = deliveryModes;
    }

    public List<ECSDeliveryMode> getDeliveryModes() {
        return deliveryModes;
    }
}
