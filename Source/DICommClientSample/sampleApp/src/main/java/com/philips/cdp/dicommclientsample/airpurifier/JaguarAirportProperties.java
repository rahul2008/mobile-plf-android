package com.philips.cdp.dicommclientsample.airpurifier;

import com.google.gson.annotations.SerializedName;

/**
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
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
