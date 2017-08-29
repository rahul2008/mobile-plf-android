/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.Date;

public class SessionDataPortProperties implements PortProperties {

    private final String KEY_TIME = "time";
    private final String KEY_RTC = "rtc_idx";
    private final String KEY_SI = "si";
    private final String KEY_WASO = "waso";
    private final String KEY_TST = "tst";
    private final String KEY_DST = "dst";
    private final String KEY_SOL = "sol";
    private final String KEY_AD = "ad";
    private final String KEY_NRT = "nrt";
    private final String KEY_CSW = "cswa";
    private final String KEY_NRA = "nra";
    private final String KEY_NRI = "nri";
    private final String KEY_LAST = "last";
    private final String KEY_GOOD = "good";
    private final String KEY_TYPE = "type";

    @SerializedName(KEY_TIME)
    private Long time;

    @SerializedName(KEY_RTC)
    private Long rtc;

    @SerializedName(KEY_SI)
    private Long sleepIntentOffset;

    @SerializedName(KEY_WASO)
    private Long wakeUpAfterSleep;

    @SerializedName(KEY_TST)
    private Long totalSleepTime;

    @SerializedName(KEY_DST)
    private Long deepSleepTime;

    @SerializedName(KEY_SOL)
    private Long sleepOnsetLatency;

    @SerializedName(KEY_AD)
    private Long arousalDensity;

    @SerializedName(KEY_NRT)
    private Long numberOfTones;

    @SerializedName(KEY_NRA)
    private Long numberOfArousals;

    @SerializedName(KEY_NRI)
    private Long numberOfInterruptions;

    @SerializedName(KEY_CSW)
    private Long cumulativeSlowWaveActivity;

    @SerializedName(KEY_LAST)
    private Integer lastSleepStage;

    @SerializedName(KEY_GOOD)
    private Boolean goodStudyIndicator;

    @SerializedName(KEY_TYPE)
    private String sessionType;

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

    @Nullable
    public Long getRTC() {
        return rtc;
    }

    @Nullable
    public Long getSleepIntentOffset() {
        return sleepIntentOffset;
    }

    @Nullable
    public Long getWakeUpAfterSleep() {
        return wakeUpAfterSleep;
    }

    @Nullable
    public Long getTotalSleepTime() {
        return totalSleepTime;
    }

    @Nullable
    public Long getDeepSleepTime() {
        return deepSleepTime;
    }

    @Nullable
    public Long getSleepOnsetLatency() {
        return sleepOnsetLatency;
    }

    @Nullable
    public Long getArousalDensity() {
        return arousalDensity;
    }

    @Nullable
    public Long getNumberOfTones() {
        return numberOfTones;
    }

    @Nullable
    public Long getCumulativeSlowWaveActivity() {
        return cumulativeSlowWaveActivity;
    }

    @Nullable
    public Long getNumberOfArousals() {
        return numberOfArousals;
    }

    @Nullable
    public Long getNumberOfInterruptions() {
        return numberOfInterruptions;
    }

    @Nullable
    public Integer getLastSleepStage() {
        return lastSleepStage;
    }

    @Nullable
    public Boolean isGoodStudyIndicator() {
        return goodStudyIndicator;
    }

    @Nullable
    public String getSessionType() {
        return sessionType;
    }

    public boolean isGhostSession(){
        return totalSleepTime < 1;
    }

    public boolean anyFieldsMissing() {
        return time == null || rtc == null || sleepIntentOffset == null || wakeUpAfterSleep == null || totalSleepTime == null
                || deepSleepTime == null || sleepOnsetLatency == null || arousalDensity == null
                || numberOfTones == null || cumulativeSlowWaveActivity == null || numberOfArousals == null || numberOfInterruptions == null
                || goodStudyIndicator == null || sessionType == null || sessionType.isEmpty() || lastSleepStage == null;
    }
}
