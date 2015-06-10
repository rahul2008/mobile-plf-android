package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.List;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNDataSleep extends SHNData {
    private final List<SleepPhase> sleepPhases;
    private final long latency;
    private final long timeInBed;
    private final long sleepDuration;
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
