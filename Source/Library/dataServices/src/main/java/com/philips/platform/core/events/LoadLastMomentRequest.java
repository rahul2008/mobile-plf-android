/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.listeners.DBFetchRequestListner;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LoadLastMomentRequest extends Event {
    @NonNull
    private final String type;

    public DBFetchRequestListner getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    private final DBFetchRequestListner dbFetchRequestListner;
    public LoadLastMomentRequest(@NonNull String type, DBFetchRequestListner dbRequestListener) {
        this.type = type;
        this.dbFetchRequestListner = dbRequestListener;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
