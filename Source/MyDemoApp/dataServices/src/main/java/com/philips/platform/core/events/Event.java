/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class Event {

    public static final int NO_REFERENCE_ID = -1;
    private static int eventIdCounter = 0;

    private final int eventId;
    private final int referenceId;

    public Event() {
        this(NO_REFERENCE_ID);
    }

    public Event(final int referenceId) {
        this.eventId = ++eventIdCounter;
        this.referenceId = referenceId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getReferenceId() {
        return referenceId;
    }
}
