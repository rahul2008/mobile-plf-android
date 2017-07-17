package com.philips.platform.datasync.moments;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentsDataFetcher extends DataFetcher {
    public static final String TAG = "MomentsDataFetcher";

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

    UCoreMomentsHistory momentsHistory;

    @Override
    @CheckResult
    @Nullable
    public RetrofitError fetchDataSince(@Nullable final DateTime sinceTimestamp) {

        if (isUserInvalid()) {
            return null;
        }
        try {
            String momentsLastSyncUrl = accessProvider.getMomentLastSyncTimestamp();

            final MomentsClient client = uCoreAdapter.getAppFrameworkClient(MomentsClient.class,
                    accessProvider.getAccessToken(), gsonConverter);

            if(client==null) return null;

            if (client != null) {
                DSLog.i(DSLog.LOG, "Moments DataFetching started");
                momentsHistory = client.getMomentsHistory(accessProvider.getUserId(),
                        accessProvider.getUserId(), momentsLastSyncUrl);

                accessProvider.saveLastSyncTimeStamp(momentsHistory.getSyncurl(), UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY);

                List<UCoreMoment> uCoreMoments = momentsHistory.getUCoreMoments();
                if (uCoreMoments == null || uCoreMoments.size() <= 0) {
                    return null;
                }
                DSLog.i(DSLog.LOG, "DataPullSynchronize Moments fetch Success before convert");
                List<Moment> moments = converter.convert(uCoreMoments);
                DSLog.i(DSLog.LOG, "DataPullSynchronize Moments fetch Success after converting");
                if(moments!=null) {
                    eventing.post(new BackendMomentListSaveRequest(moments, null));
                }
            }
            DSLog.e(DSLog.LOG, "DataPullSynchronize fetchMomentWithType send null");
            return null;
        } catch (RetrofitError ex) {
            //TODO: - Veritcals should be forced to call this
            DSLog.e(TAG, "RetrofitError: " + ex.getMessage() + ex);
            eventing.post(new BackendDataRequestFailed(ex));
            onError(ex);
            return ex;
        }
    }

    protected boolean isUserInvalid() {
        final String accessToken = accessProvider.getAccessToken();
        return !accessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }
}
