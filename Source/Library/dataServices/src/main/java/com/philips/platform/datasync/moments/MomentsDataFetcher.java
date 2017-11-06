/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.moments;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class MomentsDataFetcher extends DataFetcher {
    public static final String TAG = "MomentsDataFetcher";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String LAST_MODIFIED_START_DATE = "LAST_MODIFIED_START_DATE";
    private static final String LAST_MODIFIED_END_DATE = "LAST_MODIFIED_END_DATE";

    @NonNull
    private final MomentsConverter converter;

    @Inject
    Eventing eventing;

    @NonNull
    private final GsonConverter gsonConverter;

    @Inject
    UCoreAccessProvider accessProvider;

    @Inject
    public MomentsDataFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                              @NonNull final MomentsConverter converter,
                              @NonNull final GsonConverter gsonConverter) {
        super(uCoreAdapter);
        this.converter = converter;
        this.gsonConverter = gsonConverter;
        DataServicesManager.getInstance().getAppComponant().injectMomentsDataFetcher(this);
    }


    @Override
    @CheckResult
    @Nullable
    public RetrofitError fetchData() {
        if (isUserInvalid()) {
            return null;
        }

        try {
            String momentsLastSyncUrl = accessProvider.getMomentLastSyncTimestamp();

            final MomentsClient client = uCoreAdapter.getAppFrameworkClient(MomentsClient.class,
                    accessProvider.getAccessToken(), gsonConverter);

            if (client == null) return null;

            if (client != null) {
                UCoreMomentsHistory momentsHistory = client.getMomentsHistory(accessProvider.getUserId(),
                        accessProvider.getUserId(), momentsLastSyncUrl);

                accessProvider.saveLastSyncTimeStamp(momentsHistory.getSyncurl(), UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY);

                List<UCoreMoment> uCoreMoments = momentsHistory.getUCoreMoments();
                if (uCoreMoments == null || uCoreMoments.size() <= 0) {
                    return null;
                }

                List<Moment> moments = converter.convert(uCoreMoments);
                if (moments != null) {
                    eventing.post(new BackendMomentListSaveRequest(moments, null));
                }
            }
            return null;
        } catch (RetrofitError ex) {
            eventing.post(new BackendDataRequestFailed(ex));
            onError(ex);
            return ex;
        }
    }

    @Override
    public void fetchDataByDateRange(String startDate, String endDate) {
        if (isUserInvalid()) {
            throw RetrofitError.unexpectedError("", new IllegalStateException("User is not logged in"));
        }

        final MomentsClient client = uCoreAdapter.getAppFrameworkClient(MomentsClient.class, accessProvider.getAccessToken(), gsonConverter);
        if (client == null) {
            throw RetrofitError.unexpectedError("", new IllegalStateException("Client is not initialized"));
        }

        UCoreMomentsHistory momentHistory;

        Map<String, String> timeStamp;
        timeStamp = accessProvider.getLastSyncTimeStampByDateRange(startDate, endDate);

        do {
            momentHistory = client.fetchMomentByDateRange(accessProvider.getUserId(), accessProvider.getUserId(),
                    timeStamp.get(START_DATE), timeStamp.get(END_DATE), timeStamp.get(LAST_MODIFIED_START_DATE), timeStamp.get(LAST_MODIFIED_END_DATE), 3);
            updateMomentsToDB(momentHistory);
            timeStamp = accessProvider.getLastSyncTimeStampByDateRange(momentHistory.getSyncurl());
        } while (momentHistory.getUCoreMoments().size() <= 1);
    }

    protected boolean isUserInvalid() {
        final String accessToken = accessProvider.getAccessToken();
        return !accessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }

    private void updateMomentsToDB(UCoreMomentsHistory momentsHistory) {
        List<UCoreMoment> uCoreMoments = momentsHistory.getUCoreMoments();
        if (uCoreMoments != null && !uCoreMoments.isEmpty()) {
            List<Moment> moments = converter.convert(uCoreMoments);
            eventing.post(new BackendMomentListSaveRequest(moments, null));
        }
    }
}
