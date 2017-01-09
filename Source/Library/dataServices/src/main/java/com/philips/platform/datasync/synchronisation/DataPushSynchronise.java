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

import java.util.List;
import java.util.concurrent.Executor;

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
    private final Executor executor;

    @Inject
    Eventing eventing;

    DataServicesManager mDataServicesManager;

    public DataPushSynchronise(@NonNull final List<? extends DataSender> senders,
                               @NonNull final Executor executor) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.mAppComponent.injectDataPushSynchronize(this);
        this.senders = senders;
        this.executor = executor;
    }

    public void startSynchronise(final int eventId) {
        DSLog.i("***SPO***", "In startSynchronise - DataPushSynchronize");
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

    public void onEventAsync(GetNonSynchronizedDataResponse response) {
        DSLog.i("***SPO***", "DataPushSynchronize GetNonSynchronizedDataResponse");
        synchronized (this) {
            startAllSenders(response);
        }
        mDataServicesManager.setPushComplete(true);
    }

    private void startAllSenders(final GetNonSynchronizedDataResponse nonSynchronizedData) {
        DSLog.i("***SPO***", "DataPushSynchronize startAllSenders");
        for (final com.philips.platform.datasync.synchronisation.DataSender sender : senders) {
            DSLog.i("***SPO***", "DataPushSynchronize startAllSenders inside loop");
            sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
        }
    }
}