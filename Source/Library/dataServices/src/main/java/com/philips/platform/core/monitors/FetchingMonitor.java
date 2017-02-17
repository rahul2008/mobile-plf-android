/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
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
    public void onEventAsync(LoadTimelineEntryRequest event) {
        try {
            dbInterface.fetchMoments(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType(), event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(GetNonSynchronizedDataRequest event) {
        DSLog.i("***SPO***", "In Fetching Monitor GetNonSynchronizedDataRequest");

        Map<Class, List<?>> dataToSync = new HashMap<>();

        DSLog.i("***SPO***", "In Fetching Monitor before putMomentsForSync");
        dataToSync = momentsSegregator.putMomentsForSync(dataToSync);

        DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse");
        dataToSync = consentsSegregator.putConsentForSync(dataToSync);

        DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse for UC");

        try {
            dataToSync = dbInterface.putUserCharacteristicsForSync(dataToSync);
        } catch (SQLException e) {
            e.printStackTrace();
            dataToSync.put(Characteristics.class, null);
        }

        DSLog.i("***SPO***", "In Fetching Monitor before sending GetNonSynchronizedDataResponse for UC");
        dataToSync = settingsSegregator.putSettingsForSync(dataToSync);

        eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(LoadMomentsRequest event) {
        try {
            if (event.hasType()) {
                DSLog.i(DSLog.LOG, "pabitra LoadMomentsRequest monitor fetchMomentWithType");
                dbInterface.fetchMoments(event.getDbRequestListener(), event.getTypes());
            } else if (event.hasID()) {
                dbInterface.fetchMomentById(event.getMomentID(), event.getDbRequestListener());
            } else {
                dbInterface.fetchMoments(event.getDbRequestListener());
            }
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(LoadConsentsRequest event) {
        try {
            dbInterface.fetchConsentDetails(event.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbRequestListener());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(GetNonSynchronizedMomentsRequest event) {
        DSLog.i("**SPO**", "in Fetching Monitor GetNonSynchronizedMomentsRequest");

        List<? extends Moment> ormMomentList = null;
        List<? extends ConsentDetail> consentDetails = null;
        try {
            ormMomentList = (List<? extends Moment>) dbInterface.fetchNonSynchronizedMoments();
        } catch (SQLException e) {
            //dbInterface.postError(e, event.getDbRequestListener());
        }

        try {
            consentDetails = (List<? extends ConsentDetail>) dbInterface.fetchConsentDetails();
        } catch (SQLException e) {
            //dbInterface.postError(e, event.getDbRequestListener());
        }

        eventing.post(new GetNonSynchronizedMomentsResponse(ormMomentList, consentDetails));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(LoadUserCharacteristicsRequest loadUserCharacteristicsRequest) {
        try {
            dbInterface.fetchCharacteristics(loadUserCharacteristicsRequest.getDbRequestListener());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(LoadSettingsRequest loadSettingsRequest) {

        try {
            dbInterface.fetchSettings(loadSettingsRequest.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.postError(e, loadSettingsRequest.getDbRequestListener());
        }
    }
}
