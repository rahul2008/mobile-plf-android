/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Moment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class MomentEvent extends Event {
    Moment moment;

    public MomentEvent(final Moment moment) {
        this.moment = moment;
    }

    public MomentEvent(final int referenceId, final Moment moment) {
        super(referenceId);
        this.moment = moment;
    }

    public Moment getMoment() {
        return moment;
    }
}
