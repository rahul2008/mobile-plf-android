/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBChangeListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentDataSenderCreatedRequest extends ListEvent<Moment> {

    private final DBChangeListener dbChangeListener;
    public MomentDataSenderCreatedRequest(@NonNull final List<? extends Moment> dataList,DBChangeListener dbChangeListener) {
        super(dataList);

        this.dbChangeListener = dbChangeListener;
    }

    public DBChangeListener getDbChangeListener() {
        return dbChangeListener;
    }
}
