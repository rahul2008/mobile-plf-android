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

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private Executor executor;

    @NonNull
    private final List<? extends com.philips.platform.datasync.synchronisation.DataFetcher> fetchers;

    @Inject
    Eventing eventing;

    private volatile RetrofitError fetchResult;

    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    DataServicesManager mDataServicesManager;

    @Inject
    public DataPullSynchronise(@NonNull final List<? extends DataFetcher> fetchers,
                               @NonNull final Executor executor) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.getAppComponant().injectDataPullSynchronize(this);
        this.fetchers = fetchers;
    }


    private boolean isSyncStarted() {
        return numberOfRunningFetches.get() > 0;
    }


    public void startFetching(final DateTime lastSyncDateTime, final int referenceId, final DataFetcher fetcher) {
        DSLog.i("**SPO**","In Data Pull Synchronize startFetching");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                DSLog.i("**SPO**","In Data Pull Synchronize startFetching execute");
                preformFetch(fetcher, lastSyncDateTime, referenceId);
            }
        });
    }

    public void startSynchronise(@Nullable final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i("***SPO***","In startSynchronise - DataPullSynchronize");
        this.lastSyncDateTime = lastSyncDateTime;
        this.referenceId = referenceId;
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if(!isLoggedIn){
            DSLog.i("***SPO***","DataPullSynchronize isLogged-in is false");
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }

        if (!isSyncStarted()) {
            DSLog.i("***SPO***","DataPullSynchronize isLogged-in is true");
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

    private void preformFetch(final DataFetcher fetcher, final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i("**SPO**","In Data Pull Synchronize preformFetch");
        RetrofitError resultError = fetcher.fetchDataSince(lastSyncDateTime);
        updateResult(resultError);

        int jobsRunning = numberOfRunningFetches.decrementAndGet();
        DSLog.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning);

        if (jobsRunning <= 0) {
            DSLog.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning + "calling report result");
            reportResult(fetchResult, referenceId);
        }
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        DSLog.i("**SPO**","In Data Pull Synchronize reportResult");
        if (result == null) {
            DSLog.i("**SPO**","In Data Pull Synchronize reportResult is OK call postOK");
            postOk();
        } else {
            DSLog.i("**SPO**","In Data Pull Synchronize reportResult is not OK call postError");
            postError(referenceId, result);
        }
       // eventing.post(new WriteDataToBackendRequest());
        shutdownAndAwaitTermination(((ExecutorService)executor));
    }

    private void postError(final int referenceId, final RetrofitError error) {
        DSLog.i("**SPO**","Error DataPullSynchronize postError" + error.getMessage());
        synchronisationManager.dataPullFail(error);
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void postOk() {
        DSLog.i("**SPO**","In Data Pull Synchronize postOK");
        synchronisationManager.dataPullSuccess();
       // eventing.post(new ReadDataFromBackendResponse(referenceId, null));
    }

    private void initFetch(int size) {
        DSLog.i("**SPO**","In Data Pull Synchronize initFetch");
        numberOfRunningFetches.set(size);
        fetchResult = null;
    }

    private void fetchData(final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i("**SPO**", "In Data Pull Synchronize fetchData");
        initFetch(fetchers.size());
        executor = Executors.newFixedThreadPool(20);
        for (DataFetcher fetcher : fetchers) {
            startFetching(lastSyncDateTime, referenceId, fetcher);
        }
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
