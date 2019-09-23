/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.address;

import java.io.Serializable;
import java.util.List;

public class GetShippingAddressData implements Serializable {

    private List<ECSAddress> addresses;

    public List<ECSAddress> getAddresses() {
        return addresses;
    }
}
