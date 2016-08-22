/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import java.util.Date;
import java.util.List;

public class SHNDataSleep extends SHNData {
    private final List<SleepPhase> sleepPhases;
    private final long latency;         // minutes
    private final long timeInBed;       // minutes
    private final long sleepDuration;   // minutes
    private final Date startDate;
    private final int interruptions;

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.Sleep;
    }

    public enum SleepState {
        Awake, Asleep
    }

    public SHNDataSleep(List<SleepPhase> sleepPhases, long latency, long timeInBed, long sleepDuration, Date startDate, int interruptions) {
        this.sleepPhases = sleepPhases;
        this.latency = latency;
        this.timeInBed = timeInBed;
        this.sleepDuration = sleepDuration;
        this.startDate = startDate;
        this.interruptions = interruptions;
    }

    public static class SleepPhase {
        private final SleepState sleepState;
        private final int duration;

        public SleepPhase(SleepState sleepState, int duration) {
            this.sleepState = sleepState;
            this.duration = duration;
        }

        public SleepState getSleepState() {
            return sleepState;
        }

        public int getDuration() {
            return duration;
        }
    }

    public List<SleepPhase> getSleepPhases() {
        return sleepPhases;
    }

    public long getOnSetLatency() {
        return latency;
    }

    public long getTimeInBed() {
        return timeInBed;
    }

    public long getSleepDuration() {
        return sleepDuration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getInterruptions() {
        return interruptions;
    }
}
