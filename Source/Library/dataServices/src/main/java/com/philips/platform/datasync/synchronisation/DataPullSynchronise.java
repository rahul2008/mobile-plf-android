/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.insights.InsightDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.settings.SettingsDataFetcher;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;

@SuppressWarnings("unchecked")
public class DataPullSynchronise {

    @Inject
    UCoreAccessProvider accessProvider;

    @Nullable
    private DateTime lastSyncDateTime;
    private int referenceId;

    @Inject
    SynchronisationManager synchronisationManager;

    @NonNull
    protected Executor executor;

    @NonNull
    protected List<? extends DataFetcher> fetchers;

    @Inject
    MomentsDataFetcher momentsDataFetcher;

    @Inject
    ConsentsDataFetcher consentsDataFetcher;

    @Inject
    SettingsDataFetcher settingsDataFetcher;

    @Inject
    UserCharacteristicsFetcher userCharacteristicsFetcher;

    @Inject
    InsightDataFetcher insightDataFetcher;

    @Inject
    Eventing eventing;

    private volatile RetrofitError fetchResult;

    List<? extends DataFetcher> configurableFetchers;
    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    DataServicesManager mDataServicesManager;

    @Inject
    public DataPullSynchronise(@NonNull final List<? extends DataFetcher> fetchers) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.getAppComponant().injectDataPullSynchronize(this);
        executor = Executors.newFixedThreadPool(20);
        this.fetchers = fetchers;
        configurableFetchers = getFetchers();
    }


    private boolean isSyncStarted() {
        return numberOfRunningFetches.get() > 0;
    }


    public void startFetching(final DateTime lastSyncDateTime, final int referenceId, final DataFetcher fetcher) {
        DSLog.i(DSLog.LOG,"In Data Pull synchronize startFetching");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                DSLog.i(DSLog.LOG,"In Data Pull synchronize startFetching execute");
                preformFetch(fetcher, lastSyncDateTime, referenceId);
            }
        });
    }

    public void startSynchronise(@Nullable final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i(DSLog.LOG,"In startSynchronise - DataPullSynchronize");
        this.lastSyncDateTime = lastSyncDateTime;
        this.referenceId = referenceId;
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if(!isLoggedIn){
            DSLog.i(DSLog.LOG,"DataPullSynchronize isLogged-in is false");
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }

        if (!isSyncStarted()) {
            DSLog.i(DSLog.LOG,"DataPullSynchronize isLogged-in is true");
            synchronized (this) {
                fetchData(lastSyncDateTime, referenceId);
            }
        }
    }

    /*public void registerEvent() {pu
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }*/

    protected void preformFetch(final DataFetcher fetcher, final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i(DSLog.LOG,"In Data Pull synchronize preformFetch");
        RetrofitError resultError = fetcher.fetchDataSince(lastSyncDateTime);
        updateResult(resultError);

        int jobsRunning = numberOfRunningFetches.decrementAndGet();
        DSLog.i(DSLog.LOG,"In Data Pull synchronize preformFetch and jobsRunning = " + jobsRunning);

        if (jobsRunning <= 0) {
            DSLog.i(DSLog.LOG,"In Data Pull synchronize preformFetch and jobsRunning = " + jobsRunning + "calling report result");
            reportResult(fetchResult, referenceId);
        }
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        DSLog.i(DSLog.LOG,"In Data Pull synchronize reportResult");
        if (result == null) {
            DSLog.i(DSLog.LOG,"In Data Pull synchronize reportResult is OK call postOK");
            postOk();
        } else {
            DSLog.i(DSLog.LOG,"In Data Pull synchronize reportResult is not OK call postError");
            postError(referenceId, result);
        }
       // eventing.post(new WriteDataToBackendRequest());
        synchronisationManager.shutdownAndAwaitTermination(((ExecutorService)executor));
    }

    private void postError(final int referenceId, final RetrofitError error) {
        DSLog.i(DSLog.LOG,"Error DataPullSynchronize postError" + error.getMessage());
        synchronisationManager.dataPullFail(error);
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void postOk() {
        DSLog.i(DSLog.LOG,"In Data Pull synchronize postOK");
        synchronisationManager.dataPullSuccess();
       // eventing.post(new ReadDataFromBackendResponse(referenceId, null));
    }

    private void initFetch(int size) {
        DSLog.i(DSLog.LOG,"In Data Pull synchronize initFetch");
        numberOfRunningFetches.set(size);
        fetchResult = null;
    }

    private void fetchData(final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i(DSLog.LOG, "In Data Pull synchronize fetchData");

        if(configurableFetchers.size()<=0){
            DSLog.i(DSLog.LOG, "In Data Pull synchronize Zero fetchers configured");
            synchronisationManager.dataSyncComplete();
            return;
        }
        executor = Executors.newFixedThreadPool(20);
        initFetch(configurableFetchers.size());
        for (DataFetcher fetcher : configurableFetchers) {
            startFetching(lastSyncDateTime, referenceId, fetcher);
        }
    }

    private List<? extends DataFetcher> getFetchers(){
        Set<String> configurableFetchers = mDataServicesManager.getSyncTypes();

        if(configurableFetchers == null){
            return fetchers;
        }

        ArrayList<DataFetcher> fetchList = new ArrayList<>();
        ArrayList<DataFetcher> customFetchers = mDataServicesManager.getCustomFetchers();

        if (customFetchers != null && customFetchers.size() != 0) {
            for (DataFetcher customFetcher : customFetchers) {
                fetchList.add(customFetcher);
            }
        }

        for (String fetcher : configurableFetchers){
            switch (fetcher){
                case "moment":
                    fetchList.add(momentsDataFetcher);
                    break;
                case "Settings":
                    fetchList.add(settingsDataFetcher);
                    break;
                case "characteristics":
                    fetchList.add(userCharacteristicsFetcher);
                    break;
                case "consent":
                    fetchList.add(consentsDataFetcher);
                    break;
                case "insight":
                    fetchList.add(insightDataFetcher);
                    break;

            }
        }
        return fetchList;
    }
}

