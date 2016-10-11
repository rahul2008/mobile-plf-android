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
public class MomentChangeEvent extends com.philips.platform.core.events.MomentEvent {

    public MomentChangeEvent(final int referenceId, final Moment moment) {
        super(referenceId, moment);
    }
}
