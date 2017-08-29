/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class SensorDataPortProperties implements PortProperties {

    private static final int PLACEMENT_MASTOID = 0;
    private static final String BATTERY_FULL = "full";

    private final String KEY_Z = "z";
    private final String KEY_SIG = "sig";
    private final String KEY_CONN = "conn";
    private final String KEY_VALID = "valid";
    private final String KEY_VOLT = "volt";
    private final String KEY_BATT = "batt";
    private final String KEY_CHARGE = "charge";

    @SerializedName(KEY_Z)
    private Integer[] zArr;

    @SerializedName(KEY_SIG)
    private Integer[] sigArr;

    @SerializedName(KEY_CONN)
    private Boolean[] connArr;

    @SerializedName(KEY_VALID)
    private Boolean[] validArr;

    @SerializedName(KEY_VOLT)
    private Integer volt;

    @SerializedName(KEY_BATT)
    private String batt;

    @SerializedName(KEY_CHARGE)
    private String charge;

    public boolean isPlacementCorrect() throws InvalidDataException {
        verifyBooleanArray(connArr);
        return connArr[PLACEMENT_MASTOID];
    }

    public boolean isBatteryFull() throws InvalidDataException {
        if (batt == null) {
            throw new InvalidDataException();
        }
        return BATTERY_FULL.equals(batt);
    }

    private void verifyBooleanArray(Boolean[] arr) throws InvalidDataException {
        if (arr == null) {
            throw new InvalidDataException();
        }
        for (Boolean b : arr) {
            if (b == null) {
                throw new InvalidDataException();
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    void setConnArr(Boolean[] connArr) {
        this.connArr = connArr;
    }

    @VisibleForTesting
    void setBattery(String batt){
        this.batt = batt;
    }
}
