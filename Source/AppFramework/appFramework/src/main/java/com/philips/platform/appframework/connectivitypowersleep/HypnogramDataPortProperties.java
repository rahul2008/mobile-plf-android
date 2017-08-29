/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.google.gson.annotations.SerializedName;

public class HypnogramDataPortProperties extends ArraysDataPortProperties {

    private static final String KEY_TIME = "time";
    private static final String KEY_STAGE = "stage";

    @SerializedName(KEY_TIME)
    Integer[] timeOffsets;

    @SerializedName(KEY_STAGE)
    Integer[] stages;

    public int[] getTimeOffsets() throws InvalidPortPropertiesException {
        return transform(timeOffsets);
    }

    public int[] getStages() throws InvalidPortPropertiesException {
        return transform(stages);
    }
}
