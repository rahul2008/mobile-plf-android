/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentsDataSender extends DataSender {
    @Inject
    UCoreAccessProvider accessProvider;

    @Inject
    UCoreAdapter uCoreAdapter;

    @NonNull
    private final MomentsConverter momentsConverter;

    @Inject
    BaseAppDataCreator baseAppDataCreater;

    @Inject
    UserRegistrationInterface userRegistrationImpl;

    @NonNull
    private final MomentGsonConverter momentGsonConverter;

    @Inject
    Eventing eventing;

    protected final Set<Integer> momentIds = new HashSet<>();

    @Inject
    public MomentsDataSender(
            @NonNull final MomentsConverter momentsConverter,
            @NonNull final MomentGsonConverter momentGsonConverter) {

        DataServicesManager.getInstance().getAppComponent().injectMomentsDataSender(this);
        this.momentsConverter = momentsConverter;
        this.momentGsonConverter = momentGsonConverter;
    }

    @Override
    public boolean sendDataToBackend(@NonNull final List dataToSend) {
        if (dataToSend == null || dataToSend.size() == 0) return false;
        if (!accessProvider.isLoggedIn()) {
            return false;
        }

        List<Moment> momentToSync = new ArrayList<>();
        synchronized (momentIds) {
            for (Moment moment : (List<Moment>) dataToSend) {
                if (momentIds.add(moment.getId())) {
                    momentToSync.add(moment);
                }
            }
        }
        return sendMoments(momentToSync);
    }

    private boolean sendMoments(List<? extends Moment> moments) {
        if (moments == null || moments.isEmpty()) {
            return true;
        }

        boolean conflictHappened = false;
        String baseUrl = DataServicesManager.getInstance().fetchBaseUrlFromServiceDiscovery();

        MomentsClient client = uCoreAdapter.getClient(MomentsClient.class, baseUrl,
                accessProvider.getAccessToken(), momentGsonConverter);

        if (client == null) return false;
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
        } else if (shouldDeleteMoment(moment)) {
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
        return synchronisationData.getGuid().equals(-1);
    }

    private boolean isMomentNeverSynced(final SynchronisationData synchronisationData) {
        return synchronisationData == null;
    }

    private boolean shouldDeleteMoment(final Moment moment) {
        return moment.getSynchronisationData() != null && moment.getSynchronisationData().isInactive();
    }

    private boolean createMoment(MomentsClient client, final Moment moment) {
        try {
            UCoreMoment response = client.saveMoment(moment.getSubjectId(), moment.getCreatorId(),
                    momentsConverter.convertToUCoreMoment(moment));
            if (response != null) {
                addSynchronizationData(moment, response);
                updateExpirationDate(moment, response);
                postUpdatedOk(Collections.singletonList(moment));
            }
        } catch (RetrofitError error) {
            onError(error);
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    private void updateExpirationDate(Moment moment, UCoreMoment uCoreMoment) {
        moment.setExpirationDate(new DateTime(uCoreMoment.getExpirationDate(), DateTimeZone.UTC));
    }

    private boolean updateMoment(MomentsClient client, final Moment moment) {
        try {
            String momentGuid = getMomentGuid(moment.getSynchronisationData());
            UCoreMoment response = client.updateMoment(moment.getSubjectId(), momentGuid, moment.getCreatorId(),
                    momentsConverter.convertToUCoreMoment(moment));

            moment.getSynchronisationData().setVersion(response.getVersion());
            moment.setExpirationDate(new DateTime(response.getExpirationDate(), DateTimeZone.UTC));
            postUpdatedOk(Collections.singletonList(moment));

            return false;
        } catch (RetrofitError error) {
            if (error != null && isConflict(error.getResponse())) {
            } else {
                eventing.post(new BackendResponse(1, error));
                onError(error);
            }

            return isConflict(error.getResponse());
        }
    }

    @NonNull
    private String getMomentGuid(final SynchronisationData synchronisationData) {
        return synchronisationData.getGuid();
    }

    private boolean deleteMoment(final MomentsClient client, final Moment moment) {
        try {
            String momentGuid = getMomentGuid(moment.getSynchronisationData());
            if (momentGuid.equals("-1"))
                postDeletedOk(moment);
            Response response = client.deleteMoment(moment.getSubjectId(), momentGuid, moment.getCreatorId());
            if (isResponseSuccess(response)) {
                postDeletedOk(moment);
            }
        } catch (RetrofitError error) {
            onError(error);
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    private boolean isConflict(final Response response) {
        boolean isconflict = response != null && response.getStatus() == HttpURLConnection.HTTP_CONFLICT;
        return isconflict;
    }

    private boolean shouldMomentContainCreatorIdAndSubjectId(final Moment moment) {
        return isNotNullOrEmpty(moment.getCreatorId()) &&
                isNotNullOrEmpty(moment.getSubjectId());
    }

    private boolean isNotNullOrEmpty(final String string) {
        return string != null && !string.isEmpty();
    }

    private void addSynchronizationData(Moment moment, UCoreMoment uCoreMoment) {
        SynchronisationData synchronisationData =
                baseAppDataCreater.createSynchronisationData(uCoreMoment.getGuid(), false,
                        moment.getDateTime(), 1);
        moment.setSynchronisationData(synchronisationData);
    }

    private void postUpdatedOk(final List<Moment> momentList) {
        eventing.post(new MomentDataSenderCreatedRequest(momentList, null));
    }

    private void postDeletedOk(final Moment moment) {
        eventing.post(new MomentBackendDeleteResponse(moment, null));
    }

    @Override
    public Class<? extends Moment> getClassForSyncData() {
        return Moment.class;
    }
}
