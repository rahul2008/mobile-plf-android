/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBFetchRequestListner;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FetchInsightsFromDB extends Event {

    private final DBFetchRequestListner<Insight> dbFetchRequestListner;

    public DBFetchRequestListner<Insight> getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public FetchInsightsFromDB(DBFetchRequestListner<Insight> dbRequestListener) {

        this.dbFetchRequestListner = dbRequestListener;
    }
}
