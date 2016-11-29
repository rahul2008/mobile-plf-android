/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentDataSenderUpdatedRequest extends ListEvent<Moment> {

    public MomentDataSenderUpdatedRequest(@NonNull final List<? extends Moment> dataList) {
        super(dataList);
    }
}
