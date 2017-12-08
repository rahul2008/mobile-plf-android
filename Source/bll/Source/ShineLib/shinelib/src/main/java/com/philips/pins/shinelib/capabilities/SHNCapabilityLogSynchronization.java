/*
 * Copyright (c) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNLog;

/**
 * Interface to obtain the log of new measurements from a peripheral.
 * <p/>
 * Also allows setting log synchronization options.
 * Not all options are supported by all peripherals.
 *
 * @publicApi
 */
public interface SHNCapabilityLogSynchronization extends SHNCapability {
    /**
     * Possible synchronization states.
     */
    enum State {
        Idle,
        Synchronizing
    }

    /**
     * Possible options for log synchronization.
     */
    enum Option {
        AutomaticSynchronizationEnabled,
        AutomaticSynchronizationInterval,

        /**
         * Sunshine-specific options
         */
        ShouldClear,

        /**
         * Sunshine-specific options
         */
        ShouldReadHighResolutionData,

        /**
         * Moonshine-specific option
         */
        StoreToSynchronize
    }

    /**
     * Interface used to receive notifications about ongoing synchronization.
     */
    interface SHNCapabilityLogSynchronizationListener {
        /**
         * Indicates that the state of synchronization has been changed.
         *
         * @param shnCapabilityLogSynchronization that has changed the state
         */
        void onStateUpdated(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization);

        /**
         * Indicates progress in synchronization.
         *
         * @param shnCapabilityLogSynchronization that has made progress
         * @param progress                        current progress from 0 to 1
         */
        void onProgressUpdate(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, float progress);

        /**
         * Indicates that log synchronization has been finished.
         *
         * @param shnCapabilityLogSynchronization that has finished
         * @param shnLog                          log retrieved during synchronization
         * @param shnResult                       result of the log synchronization
         */
        void onLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog, SHNResult shnResult);

        /**
         * Indicates that log synchronization has failed.
         *
         * @param shnCapabilityLogSynchronization that has failed
         * @param shnResult                       error that occurred during synchronization
         */
        void onLogSynchronizationFailed(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNResult shnResult);

        /**
         * Provides intermediate log retrieved during the synchronization process.
         *
         * @param shnCapabilityLogSynchronization that produced the log
         * @param shnLog                          part of the log synchronized
         */
        void onIntermediateLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog);
    }

    /**
     * Attach the callback to receive synchronized log items.
     *
     * @param SHNCapabilityLogSynchronizationListener callback to receive synchronized log
     */
    void setSHNCapabilityLogSynchronizationListener(SHNCapabilityLogSynchronizationListener SHNCapabilityLogSynchronizationListener);

    /**
     * Returns the current state of the capability.
     *
     * @return the current state of the capability
     */
    State getState();

    /**
     * Returns the token stored after last log synchronization.
     *
     * @return the last synchronization token
     */
    Object getLastSynchronizationToken();

    /**
     * Start synchronization from the given token.
     *
     * @param synchronizationToken to start synchronization from
     */
    void startSynchronizationFromToken(Object synchronizationToken);

    /**
     * Abort currently running synchronization.
     */
    void abortSynchronization();

    /**
     * Get log synchronization options.
     * <p/>
     * Not all options are supported by all peripherals.
     *
     * @param option            selected option
     * @param shnResultListener callback to receive retrieved value
     */
    void getValueForOption(Option option, SHNIntegerResultListener shnResultListener);

    /**
     * Set log synchronization options.
     * <p/>
     * Not all options are supported by all peripherals.
     *
     * @param value             to set for the option
     * @param option            selected option
     * @param shnResultListener result of the operation
     */
    void setValueForOption(int value, Option option, SHNResultListener shnResultListener);
}
