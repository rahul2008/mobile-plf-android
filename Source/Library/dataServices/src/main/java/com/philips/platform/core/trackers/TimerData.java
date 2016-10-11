/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TimerData {
    @NonNull
    private final List<com.philips.platform.core.trackers.TimerRun> timerRuns = new ArrayList<>();

    public TimerData() {
    }

    public TimerData(@NonNull List<com.philips.platform.core.trackers.TimerRun> runs) {
        timerRuns.addAll(runs);
    }

    public static TimerData createTimerData(DateTime start, long durationInSeconds) {
        com.philips.platform.core.trackers.TimerRun run = new com.philips.platform.core.trackers.TimerRun(start, durationInSeconds);
        return new TimerData(Collections.singletonList(run));
    }

    @NonNull
    public DateTime getInitialStart() {
        return timerRuns.size() > 0 ? timerRuns.get(0).getStart() : DateTime.now();
    }

    private long getPastDuration() {
        long pastDuration = 0;

        for (com.philips.platform.core.trackers.TimerRun run : timerRuns) {
            pastDuration += run.getDurationInSeconds();
        }

        return pastDuration;
    }

    public boolean isRunning() {
        return timerRuns.size() > 0 && getLastTimerRun().getDurationInSeconds() == 0;
    }

    public long getSeconds() {
        return getPastDuration() + getLastTimerDuration();
    }

    private int getLastTimerDuration() {
        if (isRunning()) {
            return Seconds.secondsBetween(getLastTimerRun().getStart(), DateTime.now()).getSeconds();
        } else {
            return 0;
        }
    }

    private com.philips.platform.core.trackers.TimerRun getLastTimerRun() {
        return timerRuns.get(timerRuns.size() - 1);
    }

    public void stop() {
        int lastIndex = timerRuns.size() - 1;
        long duration = getLastTimerDuration();
        if (duration > 0) {
            timerRuns.set(lastIndex, new com.philips.platform.core.trackers.TimerRun(getLastTimerRun().getStart(), duration));
        } else if (isRunning()) {
            timerRuns.remove(lastIndex);
        }
    }

    public void start() {
        timerRuns.add(new com.philips.platform.core.trackers.TimerRun(DateTime.now(), 0));
    }

    public List<com.philips.platform.core.trackers.TimerRun> getTimerRuns() {
        return new ArrayList<>(timerRuns);
    }

    public long getTotalDuration() {
        long duration = 0;
        for (com.philips.platform.core.trackers.TimerRun run : timerRuns) {
            duration = duration + run.getDurationInSeconds();
        }
        return duration;
    }
}
