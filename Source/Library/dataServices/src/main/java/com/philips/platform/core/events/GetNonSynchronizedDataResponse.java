/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedDataResponse extends Event {

    private final Map<Class, List<?>> dataToSync;

    public GetNonSynchronizedDataResponse(final int referenceId, @NonNull final Map<Class, List<?>> dataToSync) {
        super(referenceId);
        this.dataToSync = dataToSync;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> List<T> getDataToSync(Class<T> clazz) {
        return (List<T>) dataToSync.get(clazz);
    }
}
