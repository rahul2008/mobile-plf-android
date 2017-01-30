/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsConverter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class SettingsDataFetcher extends DataFetcher {
    private static final int API_VERSION = 9;
    private GsonConverter mGsonConverter;
    @Inject
    UCoreAccessProvider mUCoreAccessProvider;
    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;
    @Inject
    Eventing eventing;

    @Inject
    public SettingsDataFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                               @NonNull final GsonConverter gsonConverter) {
        super(uCoreAdapter);
        this.mGsonConverter = gsonConverter;
        DataServicesManager.getInstance().getAppComponant().injectSettingsDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        try {

            eventing.post(new SettingsBackendGetRequest());
            return null;
        } catch (RetrofitError exception) {
            eventing.post(new BackendDataRequestFailed(exception));
            return exception;
        }
    }
}
