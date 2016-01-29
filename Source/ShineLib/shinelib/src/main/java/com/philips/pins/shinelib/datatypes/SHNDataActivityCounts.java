/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 01/10/15.
 */
public class SHNDataActivityCounts extends SHNData {
    private final long activityCountPerMinute;

    public SHNDataActivityCounts(long activityCountPerMinute) {
        this.activityCountPerMinute = activityCountPerMinute;
    }

    public long getActivityCountPerMinute() {
        return activityCountPerMinute;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.ActivityCountPerMinute;
    }

    @Override
    public String toString() {
        return "ActivityCountPerMinute: " + getActivityCountPerMinute();
    }
}
