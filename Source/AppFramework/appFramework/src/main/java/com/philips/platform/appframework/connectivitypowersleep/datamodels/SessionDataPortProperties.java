/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class SessionDataPortProperties implements PortProperties {

    private final String KEY_TST = "tst";
    private final String KEY_DST = "dst";
    private final String KEY_NRI = "nri";

    @SerializedName(KEY_TST)
    private Long totalSleepTime;

    @SerializedName(KEY_DST)
    private Long deepSleepTime;

    @SerializedName(KEY_NRI)
    private Long numberOfInterruptions;

    @Nullable
    public Long getTotalSleepTime() {
        return totalSleepTime;
    }

    @Nullable
    public Long getDeepSleepTime() {
        return deepSleepTime;
    }

    @Nullable
    public Long getNumberOfInterruptions() {
        return numberOfInterruptions;
    }

    public void setTotalSleepTime(Long totalSleepTime) {
        this.totalSleepTime = totalSleepTime;
    }

    public void setDeepSleepTime(Long deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }

    public void setNumberOfInterruptions(Long numberOfInterruptions) {
        this.numberOfInterruptions = numberOfInterruptions;
    }
}
