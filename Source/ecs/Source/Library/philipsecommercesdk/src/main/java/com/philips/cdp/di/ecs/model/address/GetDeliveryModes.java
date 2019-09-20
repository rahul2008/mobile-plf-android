/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.address;

import java.io.Serializable;
import java.util.List;

public class GetDeliveryModes implements Serializable {

    private List<ECSDeliveryMode> deliveryModes;

    public void setDeliveryModes(List<ECSDeliveryMode> deliveryModes) {
        this.deliveryModes = deliveryModes;
    }

    public List<ECSDeliveryMode> getDeliveryModes() {
        return deliveryModes;
    }
}
