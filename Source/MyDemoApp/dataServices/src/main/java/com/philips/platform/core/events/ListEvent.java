/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.BaseAppData;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class ListEvent<T extends BaseAppData> extends Event {

    @NonNull
    protected final List<? extends T> list;

    public ListEvent(@NonNull final List<? extends T> list) {
        this.list = list;
    }

    public ListEvent(final int referenceId, @NonNull final List<? extends T> list) {
        super(referenceId);
        this.list = list;
    }

    @NonNull
    public List<? extends T> getList() {
        return list;
    }
}
