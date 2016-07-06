/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

import com.google.gson.annotations.SerializedName;

public class ComfortAirPortProperties implements AirPortProperties {

    public static final Integer LIGHT_ON_INTEGER = 50;

    public static final Integer LIGHT_OFF_INTEGER = 0;

    @SerializedName(KEY_LIGHT_STATE)
    Integer lightOn;

    public boolean getLightOn() {
        return lightOn > 0;
    }

    @Override
    public boolean lightIsSet() {
        return lightOn != null;
    }
}
