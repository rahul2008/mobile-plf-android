package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.List;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNDataSleep extends SHNData {
    public enum SleepState {
        Awake, Asleep
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

    public abstract List<SleepPhase> getSleepPhases();
    public abstract long getOnSetLatency();
    public abstract long getTimeInBed();
    public abstract long getSleepDuration();
    public abstract Date getStartDate();
    public abstract int getInterruptions();
}
