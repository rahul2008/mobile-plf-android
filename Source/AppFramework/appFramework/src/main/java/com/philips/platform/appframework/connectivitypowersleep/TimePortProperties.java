/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class TimePortProperties implements PortProperties {

    public static final String KEY_DATETIME = "datetime";
    private static final String KEY_IDX = "idx";
    private static final String KEY_TZ = "tz";

    @SerializedName(KEY_DATETIME)
    String datetime;

    @SerializedName(KEY_IDX)
    String idx;

    @SerializedName(KEY_TZ)
    Integer tz;

    public TimePortProperties() {
        // Default constructor for Gson
    }

    public String getDatetime() throws InvalidPortPropertiesException {
        if (datetime == null) {
            throw new InvalidPortPropertiesException();
        }
        return datetime;
    }

    public String getIdx() throws InvalidPortPropertiesException {
        if (idx == null) {
            throw new InvalidPortPropertiesException();
        }
        return idx;
    }

    public Integer getTz() throws InvalidPortPropertiesException {
        if (tz == null) {
            throw new InvalidPortPropertiesException();
        }
        return tz;
    }
}
