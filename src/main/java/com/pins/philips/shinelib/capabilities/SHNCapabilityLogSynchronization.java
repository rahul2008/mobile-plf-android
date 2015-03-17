package com.pins.philips.shinelib.capabilities;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNCapabilityLogSynchronization {
    public enum SHNLogSynchronizationState {
        SHNLogSynchronizationStateIdle,
        SHNLogSynchronizationStateSynchronizing,
        SHNLogSynchronizationStateProcessing
    }
    public enum SHNLogSynchronizationOption {
        SHNLogSynchronizationOptionAutomaticSynchronizationEnabled,
        SHNLogSynchronizationOptionAutomaticSynchronizationInterval,

        // Sunshine-specific options; these will be discarded in the Moonshine impl.
        SHNLogSynchronizationOptionShouldClear,
        SHNLogSynchronizationOptionShouldReadHighResolutionData
    }
}
