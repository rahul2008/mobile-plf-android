package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNIntegerResultListener;
import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.datatypes.SHNLog;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityLogSynchronization extends SHNCapability {
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
        LogBufferFormatError, UnknownLogRecordType // SHNError.h

    }

    interface Listener {
        void onStateUpdated(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization);
        void onProgressUpdate(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, float progress);
        void onLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog, Error error);
        void onLogSynchronizationFailed(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, Error error);
    }

    void setListener(Listener listener);
    State getState();
    Object getLastSynchronizationToken();

    void getValueForOption(Option option, SHNIntegerResultListener shnResultListener);
    void setValueForOption(int value, Option option, SHNResultListener shnResultListener);

    void startSynchronizationFromToken(Object synchronizationToken);
    void abortSynchronization();
}
