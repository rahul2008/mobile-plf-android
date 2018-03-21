/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.synchronisation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.mya.catk.ConsentInteractor;
import com.philips.platform.pif.chi.ConsentDefinitionRegistry;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UserAccessProvider;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSender;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.insights.InsightDataSender;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.settings.SettingsDataSender;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RetrofitError;

@Singleton
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataPushSynchronise extends EventMonitor {

    private ExecutorService executor;

    @Inject
    UserAccessProvider userAccessProvider;

    @Inject
    Eventing eventing;

    @Inject
    SynchronisationManager synchronisationManager;

    @Inject
    MomentsDataSender momentsDataSender;

    @Inject
    ConsentDataSender consentsDataSender;

    @Inject
    SettingsDataSender settingsDataSender;

    @Inject
    InsightDataSender insightDataSender;

    @Inject
    UserCharacteristicsSender userCharacteristicsSender;

    @NonNull
    List<? extends DataSender> senders;

    @Inject
    ConsentInteractor consentInteractor;

    List<? extends DataSender> configurableSenders;

    private DataServicesManager mDataServicesManager;

    public static final String CONSENT_TYPE_MOMENT = "moment";

    public static final int version = 0;

    @Inject
    Context context;

    public DataPushSynchronise(@NonNull final List<? extends DataSender> senders) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.getAppComponent().injectDataPushSynchronize(this);
        this.senders = senders;
    }

    void startSynchronise(final int eventId) {
        if (!userAccessProvider.isLoggedIn()) {
            eventing.post(new BackendResponse(eventId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in"))));
            return;
        }

        initializeSendersAndExecutor();
        if (executor == null) {
            synchronisationManager.dataSyncComplete();
        } else {
            registerEvent();
            fetchNonSynchronizedData(eventId);
        }
    }

    private void initializeSendersAndExecutor() {
        this.configurableSenders = getSenders();
        if (configurableSenders != null && !configurableSenders.isEmpty()) {
            this.executor = Executors.newFixedThreadPool(configurableSenders.size());
        }
    }

    private void startAllSenders(final GetNonSynchronizedDataResponse nonSynchronizedData) {
        final CountDownLatch countDownLatch = new CountDownLatch(configurableSenders.size());
        for (final DataSender sender : configurableSenders) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (sender instanceof MomentsDataSender) {
                        try {
                            syncMoments(sender, nonSynchronizedData, countDownLatch);
                        } catch (Exception ex) {
                            countDownLatch.countDown();
                        }
                    } else {
                        try {
                            syncOthers(sender, nonSynchronizedData);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //Debug log
        }

        postPushComplete();
    }

    @VisibleForTesting
    void syncMoments(@NonNull final DataSender sender, @NonNull final GetNonSynchronizedDataResponse nonSynchronizedData, final CountDownLatch countDownLatch) {
        DataServicesManager.getInstance().getAppInfra().getConsentManager().fetchConsentState(ConsentDefinitionRegistry.getDefinitionByConsentType("moment"), new FetchConsentCallback() {
            @Override
            public void onGetConsentsSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                if (consentDefinitionStatus.getConsentState().equals(ConsentStates.active)) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                syncOthers(sender, nonSynchronizedData);
                            } finally {
                                countDownLatch.countDown();
                            }
                        }
                    });
                } else {
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                countDownLatch.countDown();
            }
        });
    }

    private void syncOthers(final DataSender sender, final GetNonSynchronizedDataResponse nonSynchronizedData) {
        sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
    }

    private void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    private void fetchNonSynchronizedData(int eventId) {
        eventing.post(new GetNonSynchronizedDataRequest(eventId));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetNonSynchronizedDataResponse response) {
        synchronized (this) {
            startAllSenders(response);
        }
    }

    private void postPushComplete() {
        synchronisationManager.dataSyncComplete();
    }

    private List<? extends DataSender> getSenders() {
        Set<String> configurableSenders = mDataServicesManager.getSyncTypes();

        if (configurableSenders == null || (configurableSenders != null && configurableSenders.isEmpty())) {
            return senders;
        }

        ArrayList<DataSender> dataSenders = new ArrayList<>();

        ArrayList<DataSender> customSenders = mDataServicesManager.getCustomSenders();

        if (customSenders != null && customSenders.size() != 0) {
            dataSenders.addAll(customSenders);
        }

        for (String sender : configurableSenders) {
            switch (sender) {
                case "moment":
                    dataSenders.add(momentsDataSender);
                    break;
                case "Settings":
                    dataSenders.add(settingsDataSender);
                    break;
                case "characteristics":
                    dataSenders.add(userCharacteristicsSender);
                    break;
                case "consent":
                    dataSenders.add(consentsDataSender);
                    break;
                case "insight":
                    dataSenders.add(insightDataSender);
                    break;
            }
        }
        return dataSenders;
    }
}