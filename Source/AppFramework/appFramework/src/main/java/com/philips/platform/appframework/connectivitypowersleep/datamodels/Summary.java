/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.NonNull;

import java.util.Date;

public class Summary implements Comparable<Summary> {


    public static final String BASELINE = "baseline";

    @NonNull
    private final Date date;
    private final long totalSleepTime;
    private final long deepSleepTime;

    public Summary(@NonNull Date date,long totalSleepTime, long deepSleepTime) {
        this.date = date;
        this.totalSleepTime = totalSleepTime;
        this.deepSleepTime = deepSleepTime;
    }

    public long getPosixTime() {
        return getDate().getTime() / 1000;
    }

    @NonNull
    public Date getDate() {
        return new Date(date.getTime());
    }


    public long getTotalSleepTime() {
        return totalSleepTime;
    }

    public long getDeepSleepTime() {
        return deepSleepTime;
    }


    @Override
    public int compareTo(@NonNull final Summary o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Summary session = (Summary) o;

        if (totalSleepTime != session.totalSleepTime) return false;
        if (deepSleepTime != session.deepSleepTime) return false;
        if (!date.equals(session.date)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (int) (totalSleepTime ^ (totalSleepTime >>> 32));
        result = 31 * result + (int) (deepSleepTime ^ (deepSleepTime >>> 32));
        return result;
    }

}
