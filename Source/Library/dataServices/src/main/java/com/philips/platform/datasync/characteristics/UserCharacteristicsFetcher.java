/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
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

    private static final int API_VERSION = 9;

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
        DataServicesManager.getInstance().mAppComponent.injectUserCharacteristicsFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        final UserCharacteristicsClient userCharacteristicsClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                mUCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (userCharacteristicsClient != null) {
            UCoreUserCharacteristics uCoreUserCharacteristics = userCharacteristicsClient.getUserCharacteristics(mUCoreAccessProvider.getUserId(),
                    mUCoreAccessProvider.getUserId(), API_VERSION);
            Characteristics characteristics = mUserCharacteristicsConverter.convertToCharacteristics(uCoreUserCharacteristics);
            eventing.post(new CharacteristicsBackendGetRequest(characteristics));
        }
        return null;
    }
}
