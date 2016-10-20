/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentsDataSender implements DataSender<Moment> {
    @NonNull
    private final UCoreAccessProvider accessProvider;

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @NonNull
    private final MomentsConverter momentsConverter;

    @NonNull
    private final BaseAppDataCreator baseAppDataCreater;

    @NonNull
    private final MomentGsonConverter momentGsonConverter;

    @NonNull
    private final Eventing eventing;

    protected final Set<Integer> momentIds = new HashSet<>();

    @Inject
    public MomentsDataSender(
            @NonNull final UCoreAccessProvider accessProvider,
            @NonNull final UCoreAdapter uCoreAdapter,
            @NonNull final MomentsConverter momentsConverter,
            @NonNull final BaseAppDataCreator baseAppDataCreater,
            @NonNull final MomentGsonConverter momentGsonConverter,
            @NonNull final Eventing eventing) {
        this.accessProvider = accessProvider;
        this.uCoreAdapter = uCoreAdapter;
        this.momentsConverter = momentsConverter;
        this.baseAppDataCreater = baseAppDataCreater;
        this.momentGsonConverter = momentGsonConverter;
        this.eventing = eventing;
    }

    @Override
    public boolean sendDataToBackend(@NonNull final List<? extends Moment> dataToSend) {
        if (!accessProvider.isLoggedIn()) {
            return false;
        }

        List<Moment> momentToSync = new ArrayList<>();
        synchronized (momentIds) {
            for (Moment moment : dataToSend) {
                if (momentIds.add(moment.getId())) {
                    momentToSync.add(moment);
                }
            }
        }
        return sendMoments(momentToSync);
    }

    private boolean sendMoments(List<? extends Moment> moments) {
        if(moments == null || moments.isEmpty()) {
            return true;
        }
        boolean conflictHappened = false;
        String BASE = "https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com";
        MomentsClient client = uCoreAdapter.getClient(MomentsClient.class, BASE,
                accessProvider.getAccessToken(), momentGsonConverter);

        for (Moment moment : moments) {
            if (shouldMomentContainCreatorIdAndSubjectId(moment)) {
                conflictHappened = conflictHappened || sendMomentToBackend(client, moment);
            }

            synchronized (momentIds) {
                momentIds.remove(moment.getId());
            }
        }

        return conflictHappened;
    }

    private boolean sendMomentToBackend(MomentsClient client, final Moment moment) {
        if (shouldCreateMoment(moment)) {
            return createMoment(client, moment);
        } else if(shouldDeleteMoment(moment)) {
            return deleteMoment(client, moment);
        } else {
            return updateMoment(client, moment);
        }
    }

    private boolean shouldCreateMoment(final Moment moment) {
        SynchronisationData synchronisationData = moment.getSynchronisationData();
        if (isMomentNeverSynced(synchronisationData) || isMomentNeverSyncedAndDeleted(synchronisationData)) {
            return true;
        }
        return false;
    }

    private boolean isMomentNeverSyncedAndDeleted(final SynchronisationData synchronisationData) {
        return synchronisationData.getGuid().equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
    }

    private boolean isMomentNeverSynced(final SynchronisationData synchronisationData) {
        return synchronisationData == null;
    }

    private boolean shouldDeleteMoment(final Moment moment) {
        return moment.getSynchronisationData() != null && moment.getSynchronisationData().isInactive();
    }

    private boolean createMoment(MomentsClient client, final Moment moment) {
        try {
            com.philips.platform.datasync.moments.UCoreMomentSaveResponse response = client.saveMoment(moment.getSubjectId(), moment.getCreatorId(),
                    momentsConverter.convertToUCoreMoment(moment));
            if (response != null) {
                addSynchronizationData(moment, response);
                postOk(Collections.singletonList(moment));
            }
        } catch (RetrofitError error) {
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    private boolean updateMoment(MomentsClient client, final Moment moment) {
        try {
            String momentGuid = getMomentGuid(moment.getSynchronisationData());
            Response response = client.updateMoment(moment.getSubjectId(), momentGuid, moment.getCreatorId(),
                    momentsConverter.convertToUCoreMoment(moment));
            if (isResponseSuccess(response)) {
                int currentVersion = moment.getSynchronisationData().getVersion();
                moment.getSynchronisationData().setVersion(currentVersion + 1);
                postOk(Collections.singletonList(moment));
            }
            return false;
        } catch (RetrofitError error) {
            eventing.post(new BackendResponse(1, error));

            return isConflict(error);
        }
    }

    @NonNull
    private String getMomentGuid(final SynchronisationData synchronisationData) {
        return synchronisationData.getGuid();
    }

    private boolean deleteMoment(final MomentsClient client, final Moment moment) {
        try {
            String momentGuid = getMomentGuid(moment.getSynchronisationData());
            Response response = client.deleteMoment(moment.getSubjectId(), momentGuid, moment.getCreatorId());
            if (isResponseSuccess(response)) {
                postDeletedOk(moment);
            }
        } catch (RetrofitError error) {
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    private boolean isConflict(final RetrofitError retrofitError) {
        Response response = retrofitError.getResponse();
        return response != null && response.getStatus() == HttpURLConnection.HTTP_CONFLICT;
    }

    private boolean shouldMomentContainCreatorIdAndSubjectId(final Moment moment) {
        return isNotNullOrEmpty(moment.getCreatorId()) &&
                isNotNullOrEmpty(moment.getSubjectId());
    }

    private boolean isNotNullOrEmpty(final String string) {
        return string != null && !string.isEmpty();
    }

    private void addSynchronizationData(Moment moment, com.philips.platform.datasync.moments.UCoreMomentSaveResponse uCoreMomentSaveResponse) {
        SynchronisationData synchronisationData =
                baseAppDataCreater.createSynchronisationData(uCoreMomentSaveResponse.getMomentId(), false,
                        moment.getDateTime(), 1);
        moment.setSynchronisationData(synchronisationData);
    }

    private void postOk(final List<Moment> momentList) {
        eventing.post(new BackendMomentListSaveRequest(momentList));
    }

    private void postDeletedOk(final Moment moment) {
        eventing.post(new MomentBackendDeleteResponse(moment));
    }

    @Override
    public Class<? extends Moment> getClassForSyncData() {
        return Moment.class;
    }
}
