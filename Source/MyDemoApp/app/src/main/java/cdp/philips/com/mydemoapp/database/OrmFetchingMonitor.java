/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadLastMomentResponse;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadMomentsResponse;
import com.philips.platform.core.events.LoadTimelineEntryRequest;
import com.philips.platform.core.events.LoadTimelineEntryResponse;
import com.philips.platform.core.monitors.EventMonitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cdp.philips.com.mydemoapp.database.table.OrmMoment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmFetchingMonitor extends EventMonitor {

    @NonNull
    private OrmFetching fetching;

    public OrmFetchingMonitor(@NonNull OrmFetching fetching) {
        this.fetching = fetching;
    }

    public void onEventBackgroundThread(LoadTimelineEntryRequest event) {
        try {
            final List<OrmMoment> ormMoments = getActiveMoments(fetching.fetchMoments());


            eventing.post(new LoadTimelineEntryResponse(event.getEventId(), ormMoments));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading timeline failed", e));
        }
    }


    public void onEventBackgroundThread(LoadLastMomentRequest event) {
        try {
            OrmMoment moment = fetching.fetchLastMoment(event.getType());
            if (moment != null) {
                eventing.post(new LoadLastMomentResponse(event.getEventId(), moment));
            }
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading last entry", e));
        }
    }


    public void onEventBackgroundThread(GetNonSynchronizedDataRequest event) {
        try {
            Map<Class, List<?>> dataToSync = new HashMap<>();

            putMomentsForSync(dataToSync);
            eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading last entry", e));
        }
    }


    private void putMomentsForSync(final Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Moment> ormMomentList = fetching.fetchNonSynchronizedMoments();
        dataToSync.put(Moment.class, ormMomentList);
    }

    public void onEventBackgroundThread(LoadMomentsRequest event) {
        try {
            final List<OrmMoment> ormMoments;
            if (event.hasType()) {
                ormMoments = getActiveMoments(fetching.fetchMoments(event.getTypes()));
            } else {
                ormMoments = getActiveMoments(fetching.fetchMoments());
            }
            eventing.post(new LoadMomentsResponse(event.getEventId(), ormMoments));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Loading in graph", e));
        }
    }

    private List<OrmMoment> getActiveMoments(List<OrmMoment> ormMoments) {
        List<OrmMoment> activeOrmMoments = new ArrayList<>();
        if (ormMoments != null) {
            for (OrmMoment ormMoment : ormMoments) {
                if (ormMoment.getSynchronisationData() == null || !ormMoment.getSynchronisationData().isInactive()) {
                    activeOrmMoments.add(ormMoment);
                }
            }
        }
        return activeOrmMoments;
    }

    public void onEventBackgroundThread(GetNonSynchronizedMomentsRequest event) {
        try {
            List<? extends Moment> ormMomentList = fetching.fetchNonSynchronizedMoments();
            eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList));
        } catch (SQLException e) {
            eventing.post(new GetNonSynchronizedMomentsResponse(null));
        }
    }
}
