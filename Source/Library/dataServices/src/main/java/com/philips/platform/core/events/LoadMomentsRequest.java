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
public class LoadMomentsRequest extends Event {

    private final String type;
    private final int momentID;

    public DBFetchRequestListner getDbFetchRequestListener() {
        return dbFetchRequestListener;
    }

    public void setDbFetchRequestListener(DBFetchRequestListner dbFetchRequestListener) {
        this.dbFetchRequestListener = dbFetchRequestListener;
    }

    private String[] types;
    private DBFetchRequestListner dbFetchRequestListener;

    public LoadMomentsRequest(DBFetchRequestListner dbFetchRequestListner) {
        this.dbFetchRequestListener = dbFetchRequestListner;
        types = null;
        type = null;
        momentID = -1;
    }

    public LoadMomentsRequest(DBFetchRequestListner dbFetchRequestListner,final @NonNull String... type) {
        this.type = type[0];
        this.types = type;
        momentID = -1;
        this.dbFetchRequestListener =dbFetchRequestListner;
    }

    public LoadMomentsRequest(int momentID,DBFetchRequestListner dbFetchRequestListner) {
        this.momentID = momentID;
        this.type = null;
        this.dbFetchRequestListener =dbFetchRequestListner;
    }

    public int getMomentID() {
        return momentID;
    }

    public boolean hasType() {
        return type != null;
    }

    public boolean hasID() {
        return momentID != -1;
    }

    public Object getType() {
        return type;
    }

    public Object[] getTypes() {
        return types;
    }
}
