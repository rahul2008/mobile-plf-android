package com.philips.platform.datasync.blob;

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
import com.philips.platform.datasync.moments.MomentsClient;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.platform.datasync.moments.UCoreMoment;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;


public class BlobDataFetcher extends DataFetcher{

    @Inject
    Eventing eventing;

    @Inject
    UCoreAccessProvider accessProvider;

    @Inject
    GsonConverter gsonConverter;

    @Inject
    public BlobDataFetcher(@NonNull UCoreAdapter uCoreAdapter) {
        super(uCoreAdapter);
        DataServicesManager.getInstance().getAppComponant().injectblobDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        if (isUserInvalid()) {
            return null;
        }
        try {

            BlobClient client = uCoreAdapter.getAppFrameworkClient(BlobClient.class, accessProvider.getAccessToken(), gsonConverter);

            if (client != null) {
                client.downloadBlob("14b4f366-2c3a-46f0-a870-658ee3eb7eb0");
            }
            return null;
        } catch (RetrofitError ex) {
            DSLog.e(DSLog.LOG, "RetrofitError: " + ex.getMessage() + ex);
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
