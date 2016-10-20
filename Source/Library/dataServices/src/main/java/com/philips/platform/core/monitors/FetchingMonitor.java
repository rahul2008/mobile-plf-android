/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FetchingMonitor extends EventMonitor {

    @NonNull
    DBFetchingInterface dbInterface;

    public FetchingMonitor(DBFetchingInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    public void onEventBackgroundThread(LoadTimelineEntryRequest event) {
        try {
            dbInterface.fetchMoments();
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading timeline failed", e));
        }
    }
    
    public void onEventBackgroundThread(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType());
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading last entry", e));
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedDataRequest event) {
        Log.i("***SPO***","In Fetching Monitor GetNonSynchronizedDataRequest");
        try {
            Map<Class, List<?>> dataToSync = new HashMap<>();
            Log.i("***SPO***","In Fetching Monitor before putMomentsForSync");
            dataToSync = dbInterface.putMomentsForSync(dataToSync);
            Log.i("***SPO***","In Fetching Monitor before sending GetNonSynchronizedDataResponse");
            eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
        } catch (SQLException e) {
            Log.i("***SPO***","In Fetching Monitor before GetNonSynchronizedDataRequest error");
            eventing.post(new ExceptionEvent("Loading last entry", e));
        }
    }

    public void onEventBackgroundThread(LoadMomentsRequest event) {
        try {
            if (event.hasType()) {
                dbInterface.fetchMoments(event.getTypes());
            } else {
                dbInterface.fetchMoments();
            }
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading in graph", e));
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedMomentsRequest event) {
        Log.i("**SPO**","in Fetching Monitor GetNonSynchronizedMomentsRequest");
        try {
            List<? extends Moment> ormMomentList = (List<? extends Moment>)dbInterface.fetchNonSynchronizedMoments();
            Log.i("**SPO**","in Fetching Monitor before sending GetNonSynchronizedMomentsResponse");
            eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList));
        } catch (SQLException e) {
            eventing.post(new GetNonSynchronizedMomentsResponse(null));
        }
    }
}
