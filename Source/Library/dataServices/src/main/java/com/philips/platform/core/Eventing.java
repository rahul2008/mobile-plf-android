/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.Event;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Eventing {
    void post(@NonNull final Event event);

    void postSticky(@NonNull final Event event);

    void register(@NonNull final Object subscriber);

    void unregister(@NonNull final Object subscriber);

    boolean isRegistered(@NonNull final Object subscriber);

    void registerSticky(@NonNull final Object subscriber);

    void removeSticky(@NonNull final Event event);
}
