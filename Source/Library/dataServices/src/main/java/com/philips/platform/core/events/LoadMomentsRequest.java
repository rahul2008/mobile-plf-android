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
public class LoadMomentsRequest extends Event {

    private final Integer type;
    private final int momentID;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public void setDbRequestListener(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    private Integer[] types;
    private DBRequestListener dbRequestListener;

    public LoadMomentsRequest(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
        types = null;
        type = null;
        momentID = -1;
    }

    public LoadMomentsRequest(DBRequestListener dbRequestListener,final @NonNull Integer... type) {
        this.type = type[0];
        this.types = type;
        momentID = -1;
        this.dbRequestListener=dbRequestListener;
    }

    public LoadMomentsRequest(int momentID,DBRequestListener dbRequestListener) {
        this.momentID = momentID;
        this.type = null;
        this.dbRequestListener=dbRequestListener;
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
