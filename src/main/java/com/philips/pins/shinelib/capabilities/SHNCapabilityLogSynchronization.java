package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNLog;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityLogSynchronization extends SHNCapability {
    enum State {
        Idle
        ,Synchronizing
//        ,Processing
    }

    enum Option {
        AutomaticSynchronizationEnabled,
        AutomaticSynchronizationInterval,

        // Sunshine-specific options; these will be discarded in the Moonshine impl.
        ShouldClear,
        ShouldReadHighResolutionData
    }

    interface SHNCapabilityLogSynchronizationListener {
        void onStateUpdated(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization);
        void onProgressUpdate(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, float progress);
        void onLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog, SHNResult shnResult);
        void onLogSynchronizationFailed(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNResult shnResult);
    }

    void setSHNCapabilityLogSynchronizationListener(SHNCapabilityLogSynchronizationListener SHNCapabilityLogSynchronizationListener);
    State getState();
    Object getLastSynchronizationToken();

    void startSynchronizationFromToken(Object synchronizationToken);
    void abortSynchronization();

    void getValueForOption(Option option, SHNIntegerResultListener shnResultListener);
    void setValueForOption(int value, Option option, SHNResultListener shnResultListener);
}
