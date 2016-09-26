/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TimerRun {
    @NonNull
    private final DateTime start;

    private final long durationInSeconds;

    public TimerRun(final @NonNull DateTime start, final long durationInSeconds) {
        this.start = start;
        this.durationInSeconds = durationInSeconds;
    }

    @NonNull
    public DateTime getStart() {
        return start;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }
}
