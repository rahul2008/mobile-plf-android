/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.air;

import com.google.gson.annotations.SerializedName;

public class JaguarAirportProperties implements AirPortProperties {

    public static final String LIGHT_ON_STRING = "1";

    public static final String LIGHT_OFF_STRING = "0";

    @SerializedName(KEY_LIGHT_STATE)
    String lightOn;

    @Override
    public boolean getLightOn() {
        return Integer.parseInt(lightOn) == 1;
    }

    @Override
    public boolean lightIsSet() {
        return lightOn != null;
    }
}
