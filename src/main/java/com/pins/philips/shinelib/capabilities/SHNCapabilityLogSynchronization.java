package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNIntegerResultListener;
import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.datatypes.SHNLog;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityLogSynchronization {
    enum State {
        Idle,
        Synchronizing,
        Processing
    }

    enum Option {
        AutomaticSynchronizationEnabled,
        AutomaticSynchronizationInterval,

        // Sunshine-specific options; these will be discarded in the Moonshine impl.
        ShouldClear,
        ShouldReadHighResolutionData
    }

    enum Error {

    }

    interface Listener {
        void onStateUpdated(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization);
        void onProgressUpdate(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, float progress);
        void onLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog, Error error);
        void onLogSynchronizationFailed(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, Error error);
    }

    void setListener(Listener listener);
    State getState();
    int getLastSynchronizationToken();

    void getValueForOption(Option option, SHNIntegerResultListener shnResultListener);
    void setValueForOption(int value, Option option, SHNResultListener shnResultListener);

    void startSynchronizationFromToken(int synchronizationToken);
    void abortSynchronization();
}
