/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class SettingsDataFetcher extends DataFetcher {
    private static final int API_VERSION = 9;
    @Inject
    UCoreAccessProvider mUCoreAccessProvider;

    Eventing eventing;

    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    public SettingsDataFetcher(@NonNull final UCoreAdapter uCoreAdapter) {
        super(uCoreAdapter);
        DataServicesManager.getInstance().getAppComponant().injectSettingsDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            eventing.post(new SettingsBackendGetRequest());
        }
        return null;
    }
}
