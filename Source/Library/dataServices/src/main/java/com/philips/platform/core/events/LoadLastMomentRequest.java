/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.listeners.DBRequestListener;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LoadLastMomentRequest extends Event {
    @NonNull
    private final String type;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    private final DBRequestListener dbRequestListener;
    public LoadLastMomentRequest(@NonNull String type, DBRequestListener dbRequestListener) {
        this.type = type;
        this.dbRequestListener = dbRequestListener;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
