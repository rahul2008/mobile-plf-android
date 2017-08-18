/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.listener;

import android.support.annotation.NonNull;

/**
 * The interface DcsEventListener.
 * <p>
 * Provides notifications on DCS events.
 *
 * @publicApi
 */
public interface DcsEventListener {
    /**
     * On dcs event received.
     *
     * @param data      the event data
     * @param fromEui64 the EUI64 string belonging to this event
     * @param action    the event action
     */
    void onDCSEventReceived(@NonNull String data, @NonNull String fromEui64, @NonNull String action);
}
