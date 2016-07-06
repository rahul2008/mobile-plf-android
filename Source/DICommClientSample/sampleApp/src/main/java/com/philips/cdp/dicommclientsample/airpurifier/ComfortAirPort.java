package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
public class ComfortAirPort extends AirPort<ComfortAirPortProperties> {

    public ComfortAirPort(final NetworkNode networkNode, final CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy, ComfortAirPortProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        Map<String, Object> props = new HashMap<>();
        props.put(AirPortProperties.KEY_LIGHT_STATE, light ? ComfortAirPortProperties.LIGHT_ON_INTEGER : ComfortAirPortProperties.LIGHT_OFF_INTEGER);
        putProperties(props);
    }
}
