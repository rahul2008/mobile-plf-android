/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FetchingMonitor extends EventMonitor {

    @NonNull
    private DBFetchingInterface dbInterface;

    public FetchingMonitor(DBFetchingInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    public void onEventBackgroundThread(LoadTimelineEntryRequest event) {
        try {
            dbInterface.fetchMoments();
        } catch (SQLException e) {
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType());
        } catch (SQLException e) {
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedDataRequest event) {
        DSLog.i("***SPO***", "In Fetching Monitor GetNonSynchronizedDataRequest");
        try {
            Map<Class, List<?>> dataToSync = new HashMap<>();
            DSLog.i("***SPO***", "In Fetching Monitor before putMomentsForSync");
            dataToSync = dbInterface.putMomentsForSync(dataToSync);
            DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse");
            dataToSync = dbInterface.putConsentForSync(dataToSync);
            eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
        } catch (SQLException e) {
            DSLog.i("***SPO***", "In Fetching Monitor before GetNonSynchronizedDataRequest error");
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(LoadMomentsRequest event) {
        try {
            if (event.hasType()) {
                dbInterface.fetchMoments(event.getTypes());
            } else if (event.hasID()) {
                dbInterface.fetchMomentById(event.getMomentID());
            } else {
                dbInterface.fetchMoments();
            }
        } catch (SQLException e) {
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(LoadConsentsRequest event) {
        try {
            dbInterface.fetchConsents();
        } catch (SQLException e) {
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedMomentsRequest event) {
        DSLog.i("**SPO**", "in Fetching Monitor GetNonSynchronizedMomentsRequest");
        try {
            List<? extends Moment> ormMomentList = (List<? extends Moment>) dbInterface.fetchNonSynchronizedMoments();
            Consent consent = dbInterface.fetchConsent();
            DSLog.i("**SPO**", "in Fetching Monitor before sending GetNonSynchronizedMomentsResponse");
            if (consent == null) {
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList, null));
            } else {
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList, new ArrayList(consent.getConsentDetails())));
            }

        } catch (SQLException e) {
            dbInterface.postError(e);
        }
    }

    public void onEventBackgroundThread(LoadUserCharacteristicsRequest loadUserCharacteristicsRequest) {
        try {
            dbInterface.fetchCharacteristics();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
