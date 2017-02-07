/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RetrofitError;


@Singleton
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataPushSynchronise extends EventMonitor {

    @Inject
    UCoreAccessProvider accessProvider;

    @NonNull
    private final List<? extends DataSender> senders;

    @NonNull
    private Executor executor;

    @Inject
    Eventing eventing;

    @Inject
    SynchronisationManager synchronisationManager;

    @NonNull
    private final AtomicInteger numberOfRunningSenders = new AtomicInteger(0);


    DataServicesManager mDataServicesManager;

    public DataPushSynchronise(@NonNull final List<? extends DataSender> senders) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.getAppComponant().injectDataPushSynchronize(this);
        this.senders = senders;
    }

    public void startSynchronise(final int eventId) {
        DSLog.i("***SPO***", "In startSynchronise - DataPushSynchronize");

        if (isSyncStarted()) {
            return;
        }

        boolean isLoggedIn = accessProvider.isLoggedIn();

        if (isLoggedIn) {
            DSLog.i("***SPO***", "DataPushSynchronize isLogged-in is true");
            registerEvent();
            fetchNonSynchronizedData(eventId);
        } else {
            DSLog.i("***SPO***", "DataPushSynchronize isLogged-in is false");
            eventing.post(new BackendResponse(eventId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in"))));
        }
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    public void unRegisterEvent() {
        if (eventing.isRegistered(this)) {
            eventing.unregister(this);
        }
    }

    private void fetchNonSynchronizedData(int eventId) {
        DSLog.i("***SPO***", "DataPushSynchronize fetchNonSynchronizedData before calling GetNonSynchronizedDataRequest");
        eventing.post(new GetNonSynchronizedDataRequest(eventId));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetNonSynchronizedDataResponse response) {
        DSLog.i("***SPO***", "DataPushSynchronize GetNonSynchronizedDataResponse");
        synchronized (this) {

                startAllSenders(response);

        }
    }

    private void startAllSenders(final GetNonSynchronizedDataResponse nonSynchronizedData) {
        DSLog.i("***SPO***", "DataPushSynchronize startAllSenders");
        initPush(senders.size());
        executor = Executors.newFixedThreadPool(20);
        for (final DataSender sender : senders) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
                    int jobsRunning = numberOfRunningSenders.decrementAndGet();
                    DSLog.i("**SPO**","In Data Push Synchronize preformFetch and jobsRunning = " + jobsRunning);

                    if (jobsRunning <= 0) {
                        DSLog.i("**SPO**","In Data Push Synchronize preformPush and jobsRunning = " + jobsRunning + "calling report result");
                        postPushComplete();
                    }
                }
            });
        }
    }

    private void postPushComplete() {
        DSLog.i("***SPO***","DataPushSynchronize set Push complete");
        synchronisationManager.dataSyncComplete();
        shutdownAndAwaitTermination((ExecutorService)executor);
    }

    private boolean isSyncStarted() {
        return numberOfRunningSenders.get() > 0;
    }

    private void initPush(int size) {
        DSLog.i("**SPO**","In Data Push Synchronize initPush");
        numberOfRunningSenders.set(size);
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