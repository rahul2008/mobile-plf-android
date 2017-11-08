/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UserAccessProvider;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.insights.InsightDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.settings.SettingsDataFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit.RetrofitError;

@SuppressWarnings("unchecked")
public class DataPullSynchronise {

    private ExecutorService executor;

    @NonNull
    List<? extends DataFetcher> fetchers;

    @Inject
    SynchronisationManager synchronisationManager;

    @Inject
    UserAccessProvider userAccessProvider;

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

    private DataServicesManager dataServicesManager;

    @Inject
    public DataPullSynchronise(@NonNull final List<? extends DataFetcher> fetchers) {
        dataServicesManager = DataServicesManager.getInstance();
        dataServicesManager.getAppComponant().injectDataPullSynchronize(this);
        this.fetchers = fetchers;
    }

    void startSynchronise(final int referenceId) {
        if (!userAccessProvider.isLoggedIn()) {
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }
        initializeFetchersAndExecutor();
        if (executor == null) {
            synchronisationManager.dataSyncComplete();
        } else {
            fetchData(referenceId);
        }
    }

    void startSynchronise(String startDate, String endDate, final int referenceId) {
        if (!userAccessProvider.isLoggedIn()) {
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }

        initializeFetchersAndExecutor();
        if (executor == null) {
            synchronisationManager.dataSyncComplete();
        } else {
            fetchDataByDateRange(startDate, endDate);
        }
    }

    private void initializeFetchersAndExecutor() {
        this.configurableFetchers = getFetchers(dataServicesManager);
        if (configurableFetchers != null && !configurableFetchers.isEmpty()) {
            this.executor = Executors.newFixedThreadPool(configurableFetchers.size());
        }
    }

    private synchronized void fetchData(final int referenceId) {
        final CountDownLatch countDownLatch = new CountDownLatch(configurableFetchers.size());

        for (final DataFetcher fetcher : configurableFetchers) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RetrofitError resultError = fetcher.fetchData();
                    updateResult(resultError);
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //Debug log
        }

        reportResult(fetchResult, referenceId);
    }

    private synchronized void fetchDataByDateRange(final String startDate, final String endDate) {
        final CountDownLatch countDownLatch = new CountDownLatch(configurableFetchers.size());

        for (final DataFetcher fetcher : configurableFetchers) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        fetcher.fetchDataByDateRange(startDate, endDate);
                    }catch (RetrofitError error) {
                        synchronisationManager.dataPullFail(error);
                    }
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //Debug log
        }

        synchronisationManager.dataPullSuccess();
    }

     void postPartialSyncError(String tillDate) {
        synchronisationManager.dataPartialPullSuccess(tillDate);
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        if (result == null) {
            postOk();
        } else {
            postError(referenceId, result);
        }
    }

    private void postOk() {
        synchronisationManager.dataPullSuccess();
    }

    private void postError(final int referenceId, final RetrofitError error) {
        synchronisationManager.dataPullFail(error);
        eventing.post(new BackendResponse(referenceId, error));
    }

    private List<? extends DataFetcher> getFetchers(final DataServicesManager dataServicesManager) {
        Set<String> configurableFetchers = dataServicesManager.getSyncTypes();

        if (configurableFetchers == null | (configurableFetchers != null && configurableFetchers.isEmpty())) {
            return fetchers;
        }

        ArrayList<DataFetcher> fetchList = new ArrayList<>();
        ArrayList<DataFetcher> customFetchers = dataServicesManager.getCustomFetchers();

        if (customFetchers != null && customFetchers.size() != 0) {
            for (DataFetcher customFetcher : customFetchers) {
                fetchList.add(customFetcher);
            }
        }

        for (String fetcher : configurableFetchers) {
            switch (fetcher) {
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


