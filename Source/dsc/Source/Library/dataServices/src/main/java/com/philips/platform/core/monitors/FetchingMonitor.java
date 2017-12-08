/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.GetNonSynchronizedConsentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedConsentssResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLastMomentRequest;
import com.philips.platform.core.events.LoadLatestMomentByTypeRequest;
import com.philips.platform.core.events.LoadMomentsByDate;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.insights.InsightSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.platform.datasync.settings.SettingsSegregator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

    @Inject
    InsightSegregator insightSegregator;

    public FetchingMonitor(DBFetchingInterface dbInterface) {
        this.dbInterface = dbInterface;
        DataServicesManager.getInstance().getAppComponent().injectFetchingMonitor(this);
    }

    //Non-Sync data
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetNonSynchronizedDataRequest event) {
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync = momentsSegregator.putMomentsForSync(dataToSync);
        dataToSync = consentsSegregator.putConsentForSync(dataToSync);
        try {
            dataToSync = dbInterface.putUserCharacteristicsForSync(dataToSync);
        } catch (SQLException e) {
            dataToSync.put(Characteristics.class, null);
        }
        dataToSync = settingsSegregator.putSettingsForSync(dataToSync);
        dataToSync = insightSegregator.putInsightForSync(dataToSync);
        eventing.post(new GetNonSynchronizedDataResponse(event.getEventId(), dataToSync));
    }

    //Moments
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadLastMomentRequest event) {
        try {
            dbInterface.fetchLastMoment(event.getType(), event.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbFetchRequestListner());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadLatestMomentByTypeRequest event) {
        try {
            dbInterface.fetchLatestMomentByType(event.getType(), event.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbFetchRequestListner());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadMomentsRequest event) {
        try {
            if (event.hasType()) {
                dbInterface.fetchMoments(event.getDbFetchRequestListener(), event.getTypes());
            } else if (event.hasID()) {
                dbInterface.fetchMomentById(event.getMomentID(), event.getDbFetchRequestListener());
            } else {
                dbInterface.fetchMoments(event.getDbFetchRequestListener());
            }
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbFetchRequestListener());
        }
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadMomentsByDate event) {
        try {
            if(event.getMomentType() == null || event.getMomentType().isEmpty()) {
                dbInterface.fetchMomentsWithTimeLine(event.getStartDate(),event.getEndDate(),event.getPaginationModel(),event.getDbFetchRequestListner());
            } else {
                dbInterface.fetchMomentsWithTypeAndTimeLine(event.getMomentType(),event.getStartDate(),event.getEndDate(),event.getPaginationModel(),event.getDbFetchRequestListner());
            }
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbFetchRequestListner());
        }
    }

    //Consent
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadConsentsRequest event) {
        try {
            dbInterface.fetchConsentDetails(event.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, event.getDbFetchRequestListner());
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetNonSynchronizedConsentsRequest event) {
        List<ConsentDetail> consentDetails = null;
        try {
            consentDetails = (List<ConsentDetail>) dbInterface.fetchConsentDetails();
        } catch (SQLException e) {
            //dbInterface.postError(e, event.getDbFetchRequestListener());
        }
        eventing.post(new GetNonSynchronizedConsentssResponse(consentDetails));
    }

    //Characteristics
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadUserCharacteristicsRequest loadUserCharacteristicsRequest) {
        try {
            dbInterface.fetchCharacteristics(loadUserCharacteristicsRequest.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, loadUserCharacteristicsRequest.getDbFetchRequestListner());
        }
    }

    //Settings
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(LoadSettingsRequest loadSettingsRequest) {
        try {
            dbInterface.fetchSettings(loadSettingsRequest.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, loadSettingsRequest.getDbFetchRequestListner());
        }
    }

    //Insights
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(FetchInsightsFromDB fetchInsightsFromDB) {
        try {
            dbInterface.fetchActiveInsights(fetchInsightsFromDB.getDbFetchRequestListner());
        } catch (SQLException e) {
            dbInterface.postError(e, fetchInsightsFromDB.getDbFetchRequestListner());
        }
    }
}
