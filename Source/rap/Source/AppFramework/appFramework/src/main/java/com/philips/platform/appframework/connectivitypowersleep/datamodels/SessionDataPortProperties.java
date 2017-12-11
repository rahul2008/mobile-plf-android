/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.Date;

public class SessionDataPortProperties implements PortProperties {

    private final String KEY_TST = "tst";
    private final String KEY_DST = "dst";
    private final String KEY_NRI = "nri";
    private final String KEY_TIME = "time";
    private final String KEY_RTC = "rtc_idx";

    @SerializedName(KEY_TST)
    private Long totalSleepTime;

    @SerializedName(KEY_DST)
    private Long deepSleepTime;

    @SerializedName(KEY_NRI)
    private Long numberOfInterruptions;

    @SerializedName(KEY_TIME)
    private Long time;

    @SerializedName(KEY_RTC)
    private Long rtc;


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

    @Nullable
    public Long getRTC() {
        return rtc;
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

    @Nullable
    public Long getEpochTime() {
        return time;
    }

    @Nullable
    public Date getDate() {
        if (time == null) {
            return null;
        }
        return new Date(time * 1000);
    }

    public boolean isEmptySession(){
        return totalSleepTime == null || totalSleepTime < 1;
    }

    public boolean isSessionTimeValid(){
        return rtc != null && rtc == 0;
    }
}
