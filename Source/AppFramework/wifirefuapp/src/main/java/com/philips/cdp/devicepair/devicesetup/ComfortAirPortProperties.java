/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.devicesetup;

import com.google.gson.annotations.SerializedName;

public class ComfortAirPortProperties implements AirPortProperties {

    public static final Integer LIGHT_ON_INTEGER = 50;

    public static final Integer LIGHT_OFF_INTEGER = 0;

    @SerializedName(KEY_LIGHT_STATE)
    public Integer lightOn;

    public boolean getLightOn() {
        return lightOn > 0;
    }

    @Override
    public boolean lightIsSet() {
        return lightOn != null;
    }
}
