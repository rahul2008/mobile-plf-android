/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DataClearRequest extends Event {

    private final DBRequestListener dbRequestListener;
    public DataClearRequest(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
