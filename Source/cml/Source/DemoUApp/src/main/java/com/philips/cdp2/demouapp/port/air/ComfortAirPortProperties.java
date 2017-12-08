/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.air;

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
