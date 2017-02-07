/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadTimelineEntryRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.platform.datasync.settings.SettingsSegregator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
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

    @Inject
    SettingsSegregator settingsSegregator;

    public FetchingMonitor(DBFetchingInterface dbInterface) {
        this.dbInterface = dbInterface;
        DataServicesManager.getInstance().getAppComponant().injectFetchingMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadTimelineEntryRequest event) {
        try {
            dbInterface.fetchMoments(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e,event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType(),event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
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

            DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse for UC");
            dataToSync = settingsSegregator.putSettingsForSync(dataToSync);

            eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
        } catch (SQLException e) {
            DSLog.i("***SPO***","In Fetching Monitor before GetNonSynchronizedDataRequest error");
            dbInterface.postError(e, null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadMomentsRequest event) {
        try {
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadConsentsRequest event) {
        try {
            dbInterface.fetchConsents(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(GetNonSynchronizedMomentsRequest event) {
        DSLog.i("**SPO**","in Fetching Monitor GetNonSynchronizedMomentsRequest");
        try {
            List<? extends Moment> ormMomentList = (List<? extends Moment>)dbInterface.fetchNonSynchronizedMoments();
             List<? extends ConsentDetail> consentDetails= (List<? extends ConsentDetail>) dbInterface.fetchNonSyncConsents();
            if(consentDetails==null){
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList,null));
            }else{
                eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList, consentDetails));
            }

        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadUserCharacteristicsRequest loadUserCharacteristicsRequest) {
        try {
            dbInterface.fetchCharacteristics(loadUserCharacteristicsRequest.getDbRequestListener());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(LoadSettingsRequest loadSettingsRequest) {

        try {
            dbInterface.fetchSettings(loadSettingsRequest.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, loadSettingsRequest.getDbRequestListener());
        }
    }
}
