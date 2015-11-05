package com.philips.cdp.dicommclientsample;

import com.google.gson.annotations.SerializedName;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AirPortProperties {
    public static final String KEY_LIGHT_STATE = "aqil";

    public static final String LIGHT_ON_STRING = "1";

    public static final String LIGHT_OFF_STRING = "0";

    //aqil is the name of the light property in AirPurifier json spec
    @SerializedName(KEY_LIGHT_STATE) private String lightOn;

    public boolean getLightOn() {
        return Integer.parseInt(lightOn) == 1;
    }
}
