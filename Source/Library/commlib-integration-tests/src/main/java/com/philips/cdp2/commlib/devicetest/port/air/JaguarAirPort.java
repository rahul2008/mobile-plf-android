/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.port.air;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class JaguarAirPort extends AirPort<JaguarAirportProperties> {

    public JaguarAirPort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy, JaguarAirportProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        putProperties(AirPortProperties.KEY_LIGHT_STATE, light ? JaguarAirportProperties.LIGHT_ON_STRING : JaguarAirportProperties.LIGHT_OFF_STRING);
    }
}
