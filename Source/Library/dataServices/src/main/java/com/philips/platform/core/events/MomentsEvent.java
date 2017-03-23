/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class MomentsEvent extends Event {
    List<Moment> moments;
    DBRequestListener<Moment> dbRequestListener;
    public MomentsEvent(final List<Moment> moments, final DBRequestListener<Moment> dbRequestListener) {
        this.moments = moments;
        this.dbRequestListener=dbRequestListener;
    }

    public MomentsEvent(final List<Moment> moments) {
        this.moments = moments;
    }

    public MomentsEvent(final int referenceId, final List<Moment> moments) {
        super(referenceId);
        this.moments = moments;
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public DBRequestListener<Moment> getDbRequestListener() {
        return dbRequestListener;
    }
}
