package com.pins.philips.shinelib.datatypes;

import java.util.Date;
import java.util.List;

/**
 * Created by 310188215 on 07/05/15.
 */
public abstract class SHNDataSleep implements SHNData {
    public enum SleepState {
        Awake, Asleep
    }

    public static abstract class SleepPhase {
        public abstract SleepState getSleepState();
        public abstract int getDuration(); // In seconds?
    }

    public abstract List<SleepPhase> getSleepPhases();
    public abstract long getOnSetLatency();
    public abstract long getTimeInBed();
    public abstract long getSleepDuration();
    public abstract Date getStartDate();
    public abstract int getInterruptions();
}
