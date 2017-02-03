/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class ConsentsDataFetcher extends DataFetcher {
    public static final String TAG = "ConsentsDataFetcher";
    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    private List<Consent> consents;

    @Inject
    Eventing eventing;

    @Inject
    public ConsentsDataFetcher(@NonNull final UCoreAdapter uCoreAdapter) {
        super(uCoreAdapter);
        DataServicesManager.getInstance().getAppComponant().injectConsentsDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            eventing.post(new ConsentBackendGetRequest(1, consents));
        }
        return null;
    }

    public List<Consent> getConsents() {
        return consents;
    }

    public void setConsents(List<Consent> consents) {
        this.consents = consents;
    }

    @Override
    public RetrofitError fetchAllData() {

        return super.fetchAllData();
    }
}

