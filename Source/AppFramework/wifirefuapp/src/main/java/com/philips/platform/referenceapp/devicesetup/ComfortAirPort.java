/*
 * (C) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */
package com.philips.platform.referenceapp.devicesetup;


import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.HashMap;
import java.util.Map;

public class ComfortAirPort extends AirPort<ComfortAirPortProperties> {

    public ComfortAirPort(final CommunicationStrategy communicationStrategy) {
        super(communicationStrategy, ComfortAirPortProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        Map<String, Object> props = new HashMap<>();
        props.put(AirPortProperties.KEY_LIGHT_STATE, light ? ComfortAirPortProperties.LIGHT_ON_INTEGER : ComfortAirPortProperties.LIGHT_OFF_INTEGER);
        putProperties(props);
    }
}
