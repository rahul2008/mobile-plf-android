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
    @NonNull
    protected final UCoreAccessProvider mAccessProvider;
    private GsonConverter mGsonConverter;
    DataServicesManager mDataServicesManager;
    UserCharacteristicsConverter mUserCharacteristicsConverter;

    @Inject
    public UserCharacteristicsFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                                      @NonNull final Eventing eventing,
                                      @NonNull final GsonConverter gsonConverter,
                                      @NonNull final UserCharacteristicsConverter userCharacteristicsConverter) {
        super(uCoreAdapter, eventing);
        this.mGsonConverter = gsonConverter;
        mDataServicesManager = DataServicesManager.getInstance();
        this.mAccessProvider = mDataServicesManager.getUCoreAccessProvider();
        mUserCharacteristicsConverter = userCharacteristicsConverter;
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
                    mAccessProvider.getAccessToken(), mGsonConverter);

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
