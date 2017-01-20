/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;
import android.util.Log;

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
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FetchingMonitor extends EventMonitor {

    @NonNull
    private DBFetchingInterface dbInterface;

    @Inject
    MomentsSegregator momentsSegregator;

    @Inject
    ConsentsSegregator consentsSegregator;

    public FetchingMonitor(DBFetchingInterface dbInterface) {
        this.dbInterface = dbInterface;
        DataServicesManager.getInstance().mAppComponent.injectFetchingMonitor(this);
    }

    public void onEventBackgroundThread(LoadTimelineEntryRequest event) {
        try {
            dbInterface.fetchMoments(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e,event.getDbRequestListener());
        }
    }
    
    public void onEventBackgroundThread(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType(),event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedDataRequest event) {
        DSLog.i("***SPO***","In Fetching Monitor GetNonSynchronizedDataRequest");
        try {
            Map<Class, List<?>> dataToSync = new HashMap<>();
            DSLog.i("***SPO***","In Fetching Monitor before putMomentsForSync");
            dataToSync = momentsSegregator.putMomentsForSync(dataToSync);
            DSLog.i("***SPO***","In Fetching Monitor before sending GetNonSynchronizedDataResponse");
            dataToSync = consentsSegregator.putConsentForSync(dataToSync);
            DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse for UC");
            dataToSync = dbInterface.putUserCharacteristicsForSync(dataToSync);

            eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
        } catch (SQLException e) {
            DSLog.i("***SPO***","In Fetching Monitor before GetNonSynchronizedDataRequest error");
            dbInterface.postError(e, null);
        }
    }

    public void onEventBackgroundThread(LoadMomentsRequest event) {
        try {
           // Log.d(this.getClass().getName(),"Fetching Monitor");
            if (event.hasType()) {
                dbInterface.fetchMoments(event.getDbRequestListener(),event.getTypes());
            } else if (event.hasID()) {
                dbInterface.fetchMomentById(event.getMomentID(),event.getDbRequestListener());
            } else {
                dbInterface.fetchMoments(event.getDbRequestListener());
            }
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    public void onEventBackgroundThread(LoadConsentsRequest event) {
        try {
            dbInterface.fetchConsents(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    public void onEventBackgroundThread(GetNonSynchronizedMomentsRequest event) {
        DSLog.i("**SPO**","in Fetching Monitor GetNonSynchronizedMomentsRequest");
        try {
            List<? extends Moment> ormMomentList = (List<? extends Moment>)dbInterface.fetchNonSynchronizedMoments();
            Consent consent = dbInterface.fetchConsent(event.getDbRequestListener());
            if(consent==null){
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList,null));
            }else{
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList, new ArrayList(consent.getConsentDetails())));
            }

        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    public void onEventBackgroundThread(LoadUserCharacteristicsRequest loadUserCharacteristicsRequest) {
        try {
            dbInterface.fetchCharacteristics(loadUserCharacteristicsRequest.getDbRequestListener());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onEventBackgroundThread(LoadSettingsRequest loadSettingsRequest) {

        try {
            dbInterface.fetchSettings(loadSettingsRequest.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, loadSettingsRequest.getDbRequestListener());
        }
    }
}
