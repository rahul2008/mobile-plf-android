/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class UserCharacteristicsFetcher extends DataFetcher {
    @Inject
    UCoreAccessProvider mUCoreAccessProvider;
    private GsonConverter mGsonConverter;
    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;
    @Inject
    Eventing eventing;

    @Inject
    public UserCharacteristicsFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                                      @NonNull final GsonConverter gsonConverter) {
        super(uCoreAdapter);
        this.mGsonConverter = gsonConverter;
        DataServicesManager.mAppComponent.injectUserCharacteristicsFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        return fetchUserCharacteristics();
    }

    @Nullable
    private RetrofitError fetchUserCharacteristics() {
        try {
            final UserCharacteristicsClient userCharacteristicsClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                    mUCoreAccessProvider.getAccessToken(), mGsonConverter);

            if (userCharacteristicsClient != null) {
                Characteristics characteristics = mUserCharacteristicsConverter.convertToCharacteristics();
                eventing.post(new UserCharacteristicsSaveRequest(characteristics));
            }
            return null;
        } catch (RetrofitError exception) {
            return exception;
        }
    }
}
