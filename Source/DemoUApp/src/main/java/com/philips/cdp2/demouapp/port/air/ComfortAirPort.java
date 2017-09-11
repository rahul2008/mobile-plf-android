/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.air;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.HashMap;
import java.util.Map;

public class ComfortAirPort extends AirPort<ComfortAirPortProperties> {

    public ComfortAirPort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy, ComfortAirPortProperties.class);
    }

    @Override
    public void setLight(final boolean light) {
        Map<String, Object> props = new HashMap<>();
        props.put(AirPortProperties.KEY_LIGHT_STATE, light ? ComfortAirPortProperties.LIGHT_ON_INTEGER : ComfortAirPortProperties.LIGHT_OFF_INTEGER);
        putProperties(props);
    }
}
