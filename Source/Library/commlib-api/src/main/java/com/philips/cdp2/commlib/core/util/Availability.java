/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;

/**
 * An interface for availability. Availability is typically used to indicate if an object can
 * currently be interacted with.
 * <p>
 * In the context of eg. an {@link Appliance} this can mean
 * that there currently is a connection available to read/write to it.
 *
 * @param <T> The type of object availability is given for.
 * @publicApi
 */
public interface Availability<T extends Availability> {

    /**
     * Indicates if this object is available.
     *
     * @return true if available.
     */
    boolean isAvailable();

    /**
     * Add a listener to get notified of changes in availability.
     *
     * @param listener listener
     */
    void addAvailabilityListener(@NonNull AvailabilityListener<T> listener);

    /**
     * Remove a previously added listener.
     *
     * @param listener listener
     */
    void removeAvailabilityListener(@NonNull AvailabilityListener<T> listener);

    /**
     * A listener for {@link Availability}.
     *
     * @param <T> The type of Availability object returned when changes to availability happen.
     */
    interface AvailabilityListener<T extends Availability> {

        /**
         * Called when the availability of an object changes.
         * @param object T The object that has a changed availability.
         */
        void onAvailabilityChanged(@NonNull T object);
    }
}
