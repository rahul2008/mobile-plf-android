/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class JaguarAirPort extends AirPort<JaguarAirportProperties> {

    public JaguarAirPort(final CommunicationStrategy communicationStrategy) {
        super(communicationStrategy, JaguarAirportProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        putProperties(AirPortProperties.KEY_LIGHT_STATE, light ? JaguarAirportProperties.LIGHT_ON_STRING : JaguarAirportProperties.LIGHT_OFF_STRING);
    }
}
