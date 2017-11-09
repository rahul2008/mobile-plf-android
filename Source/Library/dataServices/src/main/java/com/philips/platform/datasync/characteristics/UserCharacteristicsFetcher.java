/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class UserCharacteristicsFetcher extends DataFetcher {
    private GsonConverter mGsonConverter;
    @Inject
    UCoreAccessProvider mUCoreAccessProvider;
    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;
    @Inject
    Eventing eventing;

    @Inject
    public UserCharacteristicsFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                                      @NonNull final GsonConverter gsonConverter) {
        super(uCoreAdapter);
        this.mGsonConverter = gsonConverter;
        DataServicesManager.getInstance().getAppComponant().injectUserCharacteristicsFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchData() {
        try {
            final UserCharacteristicsClient userCharacteristicsClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                    mUCoreAccessProvider.getAccessToken(), mGsonConverter);

            if (userCharacteristicsClient != null) {
                UCoreUserCharacteristics uCoreUserCharacteristics = userCharacteristicsClient.getUserCharacteristics(mUCoreAccessProvider.getUserId(),
                        mUCoreAccessProvider.getUserId(), UCoreAdapter.API_VERSION);


                List<Characteristics> characteristicsList = mUserCharacteristicsConverter.convertToCharacteristics(uCoreUserCharacteristics,
                        mUCoreAccessProvider.getUserId());

                if(characteristicsList!=null) {
                    eventing.post(new UCDBUpdateFromBackendRequest(characteristicsList, null));
                }
            }
            return null;
        } catch (RetrofitError exception) {
            eventing.post(new BackendDataRequestFailed(exception));
            onError(exception);
            return exception;
        }
    }

    @Override
    public RetrofitError fetchDataByDateRange(String startDate, String endDate) {
        return null;
    }
}
