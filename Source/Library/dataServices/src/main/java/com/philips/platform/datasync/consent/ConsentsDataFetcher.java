package com.philips.platform.datasync.consent;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.moments.MomentsClient;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.platform.datasync.moments.UCoreMoment;
import com.philips.platform.datasync.moments.UCoreMomentsHistory;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentsDataFetcher extends DataFetcher {
    public static final String TAG = "ConsentsDataFetcher";
    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    public ConsentsDataFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                               @NonNull final Eventing eventing) {
        super(uCoreAdapter, eventing);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            eventing.post(new ConsentBackendGetRequest(1));
        }
        return null;
    }

    @Override
    public RetrofitError fetchAllData() {
        return super.fetchAllData();
    }



}

